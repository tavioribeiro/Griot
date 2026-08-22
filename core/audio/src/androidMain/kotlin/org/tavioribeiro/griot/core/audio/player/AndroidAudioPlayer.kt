package org.tavioribeiro.griot.core.audio.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tavioribeiro.griot.core.audio.model.PlayerState
import org.tavioribeiro.griot.core.domain.book.model.AudioTrack

class AndroidAudioPlayer(
    private val context: Context,
    private val scope: CoroutineScope,
) : AudioPlayer {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _state = MutableStateFlow(PlayerState())
    override val state: StateFlow<PlayerState> = _state.asStateFlow()

    private var tickerJob: Job? = null

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.value = _state.value.copy(isPlaying = isPlaying)
                if (isPlaying) startTicker() else stopTicker()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    advanceToNextTrack()
                }
            }

            override fun onPlaybackParametersChanged(playbackParameters: androidx.media3.common.PlaybackParameters) {
                _state.value = _state.value.copy(playbackSpeed = playbackParameters.speed)
            }
        })
    }

    override fun loadQueue(tracks: List<AudioTrack>, startIndex: Int, startPositionMs: Long) {
        stopTicker()
        exoPlayer.clearMediaItems()
        tracks.forEach { track ->
            val uri = toUri(track.filePath)
            val item = MediaItem.fromUri(uri)
            exoPlayer.addMediaItem(item)
        }
        val safeIndex = startIndex.coerceIn(0, (tracks.size - 1).coerceAtLeast(0))
        exoPlayer.seekTo(safeIndex, startPositionMs.coerceAtLeast(0))
        exoPlayer.prepare()
        val duration = tracks.getOrNull(safeIndex)?.durationMs ?: 0L
        _state.value = PlayerState(
            isPlaying = false,
            currentTrackIndex = safeIndex,
            positionMs = startPositionMs.coerceAtLeast(0),
            durationMs = duration,
            playbackSpeed = _state.value.playbackSpeed,
            queue = tracks,
        )
    }

    override fun play() {
        if (_state.value.queue.isEmpty()) return
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun seekTo(positionMs: Long) {
        val idx = _state.value.currentTrackIndex
        val safe = positionMs.coerceIn(0, _state.value.durationMs.coerceAtLeast(0))
        exoPlayer.seekTo(idx, safe)
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
        exoPlayer.setPlaybackSpeed(safe)
        _state.value = _state.value.copy(playbackSpeed = safe)
    }

    override fun release() {
        stopTicker()
        exoPlayer.release()
    }

    private fun toUri(filePath: String): Uri {
        val parsed = Uri.parse(filePath)
        return if (parsed.scheme == "content") parsed else Uri.fromFile(File(filePath))
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch(Dispatchers.Main) {
            while (true) {
                delay(200)
                val pos = exoPlayer.currentPosition.coerceAtLeast(0)
                val dur = exoPlayer.duration.takeIf { it > 0 } ?: _state.value.durationMs
                val idx = exoPlayer.currentMediaItemIndex
                _state.value = _state.value.copy(
                    positionMs = pos,
                    durationMs = if (dur > 0) dur else _state.value.durationMs,
                    currentTrackIndex = idx,
                )
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    private fun advanceToNextTrack() {
        val next = _state.value.currentTrackIndex + 1
        if (next < _state.value.queue.size) {
            val dur = _state.value.queue[next].durationMs
            _state.value = _state.value.copy(
                currentTrackIndex = next,
                positionMs = 0,
                durationMs = dur,
                isPlaying = true,
            )
        } else {
            _state.value = _state.value.copy(isPlaying = false, positionMs = _state.value.durationMs)
            stopTicker()
        }
    }
}
