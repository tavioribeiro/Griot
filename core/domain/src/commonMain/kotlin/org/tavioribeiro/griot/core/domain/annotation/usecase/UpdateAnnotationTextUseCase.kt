package org.tavioribeiro.griot.core.domain.annotation.usecase

import org.tavioribeiro.griot.core.domain.annotation.model.AnnotationId
import org.tavioribeiro.griot.core.domain.annotation.repository.AnnotationRepository
import org.tavioribeiro.griot.core.domain.error.AnnotationError
import org.tavioribeiro.griot.core.domain.error.DomainResult

class UpdateAnnotationTextUseCase(
    private val annotationRepository: AnnotationRepository,
    private val nowEpochMs: () -> Long
) {
    suspend operator fun invoke(
        id: AnnotationId,
        newText: String
    ): DomainResult<Unit, AnnotationError> {
        if (newText.isBlank()) {
            return DomainResult.Error(AnnotationError.EmptyText)
        }
        return annotationRepository.updateAnnotationText(
            id = id,
            newText = newText.trim(),
            updatedAtEpochMs = nowEpochMs()
        )
    }
}