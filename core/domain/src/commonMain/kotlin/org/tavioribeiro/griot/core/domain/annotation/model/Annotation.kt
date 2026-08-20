package org.tavioribeiro.griot.core.domain.annotation.model


import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.TrackId
import org.tavioribeiro.griot.core.domain.progress.model.PlaybackConstants
import kotlin.math.max
import kotlin.math.min

@JvmInline
value class AnnotationId(val value: String)

data class Annotation(
    val id: AnnotationId,
    val bookId: BookId,
    val trackId: TrackId,
    val timestampMs: Long,
    val text: String,
    val createdAtEpochMs: Long,
    val updatedAtEpochMs: Long = createdAtEpochMs
){
    fun calculateSnippetRange(trackDurationMs: Long): SnippetRange {
        val windowMs = PlaybackConstants.SNIPPET_WINDOW_MS
        val startMs = max(0L, timestampMs - windowMs)
        val endMs = min(trackDurationMs, timestampMs + windowMs)
        return SnippetRange(startMs = startMs, endMs = endMs)
    }
}

data class SnippetRange(
    val startMs: Long,
    val endMs: Long
) {
    val durationMs: Long
        get() = endMs - startMs
}