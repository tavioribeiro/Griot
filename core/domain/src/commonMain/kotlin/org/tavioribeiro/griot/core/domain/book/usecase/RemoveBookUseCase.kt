package org.tavioribeiro.griot.core.domain.book.usecase

import org.tavioribeiro.griot.core.domain.annotation.repository.AnnotationRepository
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.repository.BookRepository
import org.tavioribeiro.griot.core.domain.book.repository.TrackRepository
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.error.RemovalError
import org.tavioribeiro.griot.core.domain.progress.repository.ProgressRepository

class RemoveBookUseCase(
    private val bookRepository: BookRepository,
    private val trackRepository: TrackRepository,
    private val annotationRepository: AnnotationRepository,
    private val progressRepository: ProgressRepository
) {
    suspend operator fun invoke(bookId: BookId): DomainResult<Unit, RemovalError> {
        return try {
            when (val result = bookRepository.getBookById(bookId)) {
                is DomainResult.Error -> return DomainResult.Error(RemovalError.BookNotFound)
                is DomainResult.Success -> Unit
            }

            when (val result = trackRepository.deleteTracksByBookId(bookId)) {
                is DomainResult.Error -> return storageError("Falha ao remover faixas do livro")
                is DomainResult.Success -> Unit
            }
            when (val result = annotationRepository.deleteAnnotationsByBookId(bookId)) {
                is DomainResult.Error -> return storageError("Falha ao remover anotações do livro")
                is DomainResult.Success -> Unit
            }
            when (val result = progressRepository.deleteProgressByBookId(bookId)) {
                is DomainResult.Error -> return storageError("Falha ao remover progresso do livro")
                is DomainResult.Success -> Unit
            }
            when (val result = bookRepository.deleteBook(bookId)) {
                is DomainResult.Error -> return storageError("Falha ao remover livro")
                is DomainResult.Success -> Unit
            }

            DomainResult.Success(Unit)
        } catch (e: Exception) {
            DomainResult.Error(RemovalError.Unknown(e))
        }
    }

    private fun storageError(message: String): DomainResult<Nothing, RemovalError> =
        DomainResult.Error(RemovalError.StorageError(message))
}