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
            //-------- RN-LIV-002: título obrigatório (não pode ser só espaços)
            if (book.title.isBlank()) {
                return DomainResult.Error(BookError.EmptyText)
            }

            //-------- RN-LIV-005: uma pasta física só pode estar vinculada a um único livro
            when (val inUse = bookRepository.isSourcePathInUse(book.sourceFolderPath)) {
                is DomainResult.Error -> inUse
                is DomainResult.Success -> {
                    if (inUse.data) {
                        DomainResult.Error(BookError.DuplicateSourcePath)
                    } else {
                        bookRepository.insertBook(book)
                    }
                }
            }
        } catch (e: Exception) {
            DomainResult.Error(BookError.Unknown(e))
        }
    }
}