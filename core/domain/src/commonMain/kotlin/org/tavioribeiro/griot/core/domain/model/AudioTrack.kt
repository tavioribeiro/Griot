package org.tavioribeiro.griot.core.domain.model

data class AudioTrack (
    val id: TrackId,
    val bookId: BookId,
    val title: String,
    val filePath: String,
    val orderIndex: Int,
    val durationMs: Long
)