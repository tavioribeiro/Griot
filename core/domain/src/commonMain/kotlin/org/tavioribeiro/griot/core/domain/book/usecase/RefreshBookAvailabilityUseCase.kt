package org.tavioribeiro.griot.core.domain.book.usecase

import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.model.BookStatus
import org.tavioribeiro.griot.core.domain.book.repository.BookRepository
import org.tavioribeiro.griot.core.domain.book.source.AudioFileSource
import org.tavioribeiro.griot.core.domain.error.BookError
import org.tavioribeiro.griot.core.domain.error.DomainResult

class RefreshBookAvailabilityUseCase(
    private val bookRepository: BookRepository,
    private val audioFileSource: AudioFileSource
) {
    suspend operator fun invoke(book: Book): DomainResult<Book, BookError> {
        return try {
            val exists = audioFileSource.folderExists(book.sourceFolderPath)
            val expectedStatus = if (exists) BookStatus.AVAILABLE else BookStatus.SOURCE_UNAVAILABLE

            if (book.status == expectedStatus) {
                return DomainResult.Success(book)
            }

            val updated = book.copy(status = expectedStatus)
            when (val result = bookRepository.updateBook(updated)) {
                is DomainResult.Error -> result
                is DomainResult.Success -> DomainResult.Success(updated)
            }
        } catch (e: Exception) {
            DomainResult.Error(BookError.Unknown(e))
        }
    }
}