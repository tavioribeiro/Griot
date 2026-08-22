package org.tavioribeiro.griot.core.audio.model

import org.tavioribeiro.griot.core.domain.book.model.AudioTrack

data class PlayerState(
    val isPlaying: Boolean = false,
    val currentTrackIndex: Int = 0,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val playbackSpeed: Float = 1f,
    val queue: List<AudioTrack> = emptyList(),
) {
    val currentTrack: AudioTrack? get() = queue.getOrNull(currentTrackIndex)
    val hasQueue: Boolean get() = queue.isNotEmpty()
}
