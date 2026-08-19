package org.tavioribeiro.griot.core.domain.book.usecase

import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.repository.BookRepository
import org.tavioribeiro.griot.core.domain.error.BookError
import org.tavioribeiro.griot.core.domain.error.DomainResult

class CreateBookUseCase(
    private val bookRepository: BookRepository,
) {
    suspend operator fun invoke(
        book: Book,
    ): DomainResult<Unit, BookError> {
        return try {
            //-------- RN-LIV-001
            if (book.title.isEmpty()) {
                return DomainResult.Error(BookError.EmptyText)
            }

            //-------- RN-LIV-005
            val isSourcePathInUse =  bookRepository.isSourcePathInUse("")
            when(isSourcePathInUse){
                is DomainResult.Success -> {
                    bookRepository.insertBook(book)
                    DomainResult.Success(Unit)
                }
                is DomainResult.Error -> {
                    DomainResult.Success(Unit)
                }
            }
        } catch (e: Exception){
            DomainResult.Error(BookError.Unknown(e))
        }
    }
}