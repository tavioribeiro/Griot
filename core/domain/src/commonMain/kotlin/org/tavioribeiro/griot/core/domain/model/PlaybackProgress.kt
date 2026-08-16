package org.tavioribeiro.griot.core.domain.model

data class PlaybackProgress(
    val bookId: BookId,
    val currentTrackId: TrackId,
    val currentPositionMs: Long,
    val playbackSpeed: Float = 1.0f,
    val isCompleted: Boolean = false,
    val lastPlayedAtEpochMs: Long
)
