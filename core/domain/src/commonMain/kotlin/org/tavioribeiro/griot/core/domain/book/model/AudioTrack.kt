package org.tavioribeiro.griot.core.domain.book.model


@JvmInline
value class TrackId(val value: String)

data class AudioTrack (
    val id: TrackId,
    val bookId: BookId,
    val title: String,
    val filePath: String,
    val orderIndex: Int,
    val durationMs: Long
)