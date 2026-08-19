package org.tavioribeiro.griot.core.domain.book.usecase

import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.repository.BookRepository
import org.tavioribeiro.griot.core.domain.error.BookError
import org.tavioribeiro.griot.core.domain.error.DomainResult

class GetBookUseCase(
    private val bookRepository: BookRepository,
    private val defaultCoverUri: String = DEFAULT_COVER_URI,
) {
    suspend operator fun invoke(bookId: BookId): DomainResult<Book, BookError> {
        return when (val result = bookRepository.getBookById(bookId)) {
            is DomainResult.Error -> result
            is DomainResult.Success -> {
                val book = result.data
                val resolvedBook = if (book.coverUri.isNullOrBlank()) {
                    book.copy(coverUri = defaultCoverUri)
                } else {
                    book
                }
                DomainResult.Success(resolvedBook)
            }
        }
    }

    companion object {
        const val DEFAULT_COVER_URI = "drawable://ic_default_cover"
    }
}