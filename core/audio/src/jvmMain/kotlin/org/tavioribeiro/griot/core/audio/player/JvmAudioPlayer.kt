package org.tavioribeiro.griot.core.audio.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.tavioribeiro.griot.core.audio.model.PlayerState
import org.tavioribeiro.griot.core.domain.book.model.AudioTrack

class JvmAudioPlayer(
    private val scope: CoroutineScope,
) : AudioPlayer {

    private val _state = MutableStateFlow(PlayerState())
    override val state: StateFlow<PlayerState> = _state.asStateFlow()

    private var tickerJob: Job? = null

    override fun loadQueue(tracks: List<AudioTrack>, startIndex: Int, startPositionMs: Long) {
        stopTicker()
        val safeIndex = startIndex.coerceIn(0, (tracks.size - 1).coerceAtLeast(0))
        val duration = tracks.getOrNull(safeIndex)?.durationMs ?: 0L
        _state.value = PlayerState(
            isPlaying = false,
            currentTrackIndex = safeIndex,
            positionMs = startPositionMs.coerceIn(0, duration),
            durationMs = duration,
            playbackSpeed = _state.value.playbackSpeed,
            queue = tracks,
        )
    }

    override fun play() {
        if (_state.value.queue.isEmpty()) return
        if (_state.value.isPlaying) return
        _state.value = _state.value.copy(isPlaying = true)
        startTicker()
    }

    override fun pause() {
        if (!_state.value.isPlaying) return
        _state.value = _state.value.copy(isPlaying = false)
        stopTicker()
    }

    override fun seekTo(positionMs: Long) {
        val dur = _state.value.durationMs
        val safe = positionMs.coerceIn(0, dur.coerceAtLeast(0))
        _state.value = _state.value.copy(positionMs = safe)
    }

    override fun skipForward(millis: Long) {
        seekTo(_state.value.positionMs + millis)
    }

    override fun skipBackward(millis: Long) {
        seekTo(_state.value.positionMs - millis)
    }

    override fun setSpeed(speed: Float) {
        val safe = speed.coerceIn(0.5f, 3f)
        _state.value = _state.value.copy(playbackSpeed = safe)
    }

    override fun release() {
        stopTicker()
        _state.value = _state.value.copy(isPlaying = false)
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            var lastMs = System.currentTimeMillis()
            while (isActive && _state.value.isPlaying) {
                delay(200)
                val now = System.currentTimeMillis()
                val delta = ((now - lastMs) * _state.value.playbackSpeed).toLong()
                lastMs = now
                val cur = _state.value
                val nextPos = cur.positionMs + delta
                if (nextPos >= cur.durationMs && cur.durationMs > 0) {
                    val nextIdx = cur.currentTrackIndex + 1
                    if (nextIdx < cur.queue.size) {
                        val nextDur = cur.queue[nextIdx].durationMs
                        _state.value = cur.copy(
                            currentTrackIndex = nextIdx,
                            positionMs = 0,
                            durationMs = nextDur,
                        )
                    } else {
                        _state.value = cur.copy(isPlaying = false, positionMs = cur.durationMs)
                        stopTicker()
                        break
                    }
                } else {
                    _state.value = cur.copy(positionMs = nextPos.coerceAtMost(cur.durationMs))
                }
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }
}
