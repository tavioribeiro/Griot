package org.tavioribeiro.griot.core.domain.book.repository

import kotlinx.coroutines.flow.Flow
import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.BookStatus
import org.tavioribeiro.griot.core.domain.error.BookError
import org.tavioribeiro.griot.core.domain.error.DomainResult



interface BookRepository {
    fun observeAllBooks(): Flow<List<Book>>
    fun observeBookById(id: BookId): Flow<Book?>
    suspend fun getBookById(id: BookId): DomainResult<Book, BookError>
    suspend fun insertBook(book: Book): DomainResult<Unit, BookError>
    suspend fun updateBook(book: Book): DomainResult<Unit, BookError>
    suspend fun updateBookStatus(id: BookId, status: BookStatus): DomainResult<Unit, BookError>
    suspend fun deleteBook(id: BookId): DomainResult<Unit, BookError>
    suspend fun isSourcePathInUse(sourceFolderPath: String): DomainResult<Boolean, BookError>
}