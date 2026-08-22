package org.tavioribeiro.griot.core.data.book.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tavioribeiro.griot.core.data.db.Book as BookRow
import org.tavioribeiro.griot.core.data.db.GriotDatabase
import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.BookStatus
import org.tavioribeiro.griot.core.domain.book.repository.BookRepository
import org.tavioribeiro.griot.core.domain.error.BookError
import org.tavioribeiro.griot.core.domain.error.DomainResult

class SqlDelightBookRepository(private val database: GriotDatabase) : BookRepository {

    override fun observeAllBooks(): Flow<List<Book>> =
        database.griotQueries.selectAllBooks()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.toDomain() } }

    override fun observeBookById(id: BookId): Flow<Book?> =
        database.griotQueries.selectBookById(id.value)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }

    override suspend fun getBookById(id: BookId): DomainResult<Book, BookError> =
        try {
            val row = database.griotQueries.selectBookById(id.value).executeAsOneOrNull()
            if (row == null) DomainResult.Error(BookError.BookNotFound)
            else DomainResult.Success(row.toDomain())
        } catch (t: Throwable) {
            DomainResult.Error(BookError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun insertBook(book: Book): DomainResult<Unit, BookError> =
        try {
            database.griotQueries.insertBook(
                id = book.id.value,
                title = book.title,
                author = book.author,
                coverUri = book.coverUri,
                sourceFolderPath = book.sourceFolderPath,
                status = book.status.name,
                createdAtEpochMs = book.createdAtEpochMs
            )
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(BookError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun updateBook(book: Book): DomainResult<Unit, BookError> =
        try {
            database.griotQueries.updateBook(
                title = book.title,
                author = book.author,
                coverUri = book.coverUri,
                sourceFolderPath = book.sourceFolderPath,
                status = book.status.name,
                createdAtEpochMs = book.createdAtEpochMs,
                id = book.id.value
            )
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(BookError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun updateBookStatus(id: BookId, status: BookStatus): DomainResult<Unit, BookError> =
        try {
            database.griotQueries.updateBookStatus(status = status.name, id = id.value)
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(BookError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun deleteBook(id: BookId): DomainResult<Unit, BookError> =
        try {
            database.griotQueries.deleteBook(id = id.value)
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(BookError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun isSourcePathInUse(sourceFolderPath: String): DomainResult<Boolean, BookError> =
        try {
            val count = database.griotQueries.sourcePathInUseCount(sourceFolderPath).executeAsOne()
            DomainResult.Success(count > 0)
        } catch (t: Throwable) {
            DomainResult.Error(BookError.StorageError(t.message ?: "DB error", t))
        }

    private fun BookRow.toDomain(): Book =
        Book(
            id = BookId(id),
            title = title,
            author = author,
            coverUri = coverUri,
            sourceFolderPath = sourceFolderPath,
            status = runCatching { BookStatus.valueOf(status) }.getOrDefault(BookStatus.AVAILABLE),
            createdAtEpochMs = createdAtEpochMs
        )
}
