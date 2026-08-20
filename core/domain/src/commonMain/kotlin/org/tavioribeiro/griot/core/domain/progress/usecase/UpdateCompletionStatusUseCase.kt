package org.tavioribeiro.griot.core.domain.progress.usecase

import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.error.ProgressError
import org.tavioribeiro.griot.core.domain.progress.repository.ProgressRepository

class UpdateCompletionStatusUseCase(
    private val progressRepository: ProgressRepository
) {

    suspend operator fun invoke(
        bookId: BookId,
        isCompleted: Boolean
    ): DomainResult<Unit, ProgressError> =
        progressRepository.updateCompletionStatus(bookId, isCompleted)
}