package org.tavioribeiro.griot.core.domain.book.usecase

import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.BookStatus
import org.tavioribeiro.griot.core.domain.book.repository.BookRepository
import org.tavioribeiro.griot.core.domain.error.BookError
import org.tavioribeiro.griot.core.domain.error.DomainResult

class GetBookUseCaseTest {

    private val bookId = BookId("book-123")

    private fun bookWithCover(coverUri: String?): Book = Book(
        id = bookId,
        title = "O Senhor dos Anéis",
        author = "J. R. R. Tolkien",
        coverUri = coverUri,
        sourceFolderPath = "/livros/tolkien",
        status = BookStatus.AVAILABLE,
        createdAtEpochMs = 1_700_000_000_000L
    )

    @Test
    fun `quando o livro nao possui capa, aplica a capa padrao com fone de ouvido`() = runTest {
        val repository = FakeBookRepository(bookWithCover(null))
        val useCase = GetBookUseCase(repository)

        val result = useCase(bookId)

        val book = (result as DomainResult.Success).data
        assertEquals(GetBookUseCase.DEFAULT_COVER_URI, book.coverUri)
        assertEquals("/livros/tolkien", book.sourceFolderPath)
    }

    @Test
    fun `quando a capa e apenas espacos, aplica a capa padrao`() = runTest {
        val repository = FakeBookRepository(bookWithCover("   "))
        val useCase = GetBookUseCase(repository)

        val result = useCase(bookId)

        val book = (result as DomainResult.Success).data
        assertEquals(GetBookUseCase.DEFAULT_COVER_URI, book.coverUri)
    }

    @Test
    fun `quando o livro possui capa, mantem a capa original`() = runTest {
        val repository = FakeBookRepository(bookWithCover("file:///capas/senhor-dos-aneis.jpg"))
        val useCase = GetBookUseCase(repository)

        val result = useCase(bookId)

        val book = (result as DomainResult.Success).data
        assertEquals("file:///capas/senhor-dos-aneis.jpg", book.coverUri)
    }

    @Test
    fun `quando o livro nao existe, propaga erro de livro nao encontrado`() = runTest {
        val repository = FakeBookRepository(null)
        val useCase = GetBookUseCase(repository)

        val result = useCase(bookId)

        val error = (result as DomainResult.Error).error
        assertEquals(BookError.BookNotFound, error)
    }

    private class FakeBookRepository(
        private val book: Book?,
    ) : BookRepository {
        override fun observeAllBooks(): Flow<List<Book>> = throw NotImplementedError()
        override fun observeBookById(id: BookId): Flow<Book?> = throw NotImplementedError()
        override suspend fun getBookById(id: BookId): DomainResult<Book, BookError> =
            if (book != null) DomainResult.Success(book) else DomainResult.Error(BookError.BookNotFound)
        override suspend fun insertBook(book: Book): DomainResult<Unit, BookError> = throw NotImplementedError()
        override suspend fun updateBook(book: Book): DomainResult<Unit, BookError> = throw NotImplementedError()
        override suspend fun updateBookStatus(id: BookId, status: BookStatus): DomainResult<Unit, BookError> =
            throw NotImplementedError()
        override suspend fun deleteBook(id: BookId): DomainResult<Unit, BookError> = throw NotImplementedError()
        override suspend fun isSourcePathInUse(sourceFolderPath: String): DomainResult<Boolean, BookError> =
            throw NotImplementedError()
    }
}

private fun runTest(block: suspend () -> Unit) =
    kotlinx.coroutines.test.runTest { block() }