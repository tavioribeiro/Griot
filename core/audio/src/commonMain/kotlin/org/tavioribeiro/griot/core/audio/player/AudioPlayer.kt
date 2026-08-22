package org.tavioribeiro.griot.core.audio.player

import kotlinx.coroutines.flow.StateFlow
import org.tavioribeiro.griot.core.audio.model.PlayerState
import org.tavioribeiro.griot.core.domain.book.model.AudioTrack

interface AudioPlayer {
    val state: StateFlow<PlayerState>
    fun loadQueue(tracks: List<AudioTrack>, startIndex: Int = 0, startPositionMs: Long = 0L)
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun skipForward(millis: Long = 15_000L)
    fun skipBackward(millis: Long = 15_000L)
    fun setSpeed(speed: Float)
    fun release()
}
