package org.tavioribeiro.griot.core.domain.annotation.usecase

import org.tavioribeiro.griot.core.domain.annotation.model.AnnotationId
import org.tavioribeiro.griot.core.domain.annotation.repository.AnnotationRepository
import org.tavioribeiro.griot.core.domain.error.AnnotationError
import org.tavioribeiro.griot.core.domain.error.DomainResult

class DeleteAnnotationUseCase(
    private val annotationRepository: AnnotationRepository
) {
    suspend operator fun invoke(id: AnnotationId): DomainResult<Unit, AnnotationError> =
        annotationRepository.deleteAnnotation(id)
}