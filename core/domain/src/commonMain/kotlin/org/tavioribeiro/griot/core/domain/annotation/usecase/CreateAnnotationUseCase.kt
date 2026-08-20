package org.tavioribeiro.griot.core.domain.annotation.usecase

import org.tavioribeiro.griot.core.domain.annotation.model.Annotation
import org.tavioribeiro.griot.core.domain.annotation.model.AnnotationId
import org.tavioribeiro.griot.core.domain.annotation.repository.AnnotationRepository
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.TrackId
import org.tavioribeiro.griot.core.domain.error.AnnotationError
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.util.RandomIdGenerator

class CreateAnnotationUseCase(
    private val annotationRepository: AnnotationRepository,
    private val newId: () -> String = { RandomIdGenerator.next() },
    private val nowEpochMs: () -> Long
) {


    suspend operator fun invoke(
        bookId: BookId,
        trackId: TrackId,
        timestampMs: Long,
        text: String
    ): DomainResult<Unit, AnnotationError> {
        if (text.isBlank()) {
            return DomainResult.Error(AnnotationError.EmptyText)
        }
        if (timestampMs < 0L) {
            return DomainResult.Error(AnnotationError.InvalidTimestamp)
        }

        val annotation = Annotation(
            id = AnnotationId(newId()),
            bookId = bookId,
            trackId = trackId,
            timestampMs = timestampMs,
            text = text.trim(),
            createdAtEpochMs = nowEpochMs()
        )
        return annotationRepository.createAnnotation(annotation)
    }
}