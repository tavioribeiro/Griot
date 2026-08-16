package org.tavioribeiro.griot.core.domain.model

import kotlin.math.max
import kotlin.math.min

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
        val windowMs = 120_000L // 2 minutos em milissegundos
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