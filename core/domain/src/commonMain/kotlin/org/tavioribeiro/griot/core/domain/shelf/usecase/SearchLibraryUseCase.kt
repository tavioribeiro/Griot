package org.tavioribeiro.griot.core.domain.shelf.usecase

import org.tavioribeiro.griot.core.domain.annotation.model.Annotation
import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.shelf.util.TextNormalizer

class SearchLibraryUseCase {

    operator fun invoke(
        query: String,
        books: List<Book>,
        annotations: List<Annotation>
    ): List<Book> {
        val normalizedQuery = TextNormalizer.normalize(query)
        if (normalizedQuery.isBlank()) {
            return books
        }

        val annotationsByBook = annotations.groupBy { it.bookId }

        return books.filter { book ->
            val matchesTitle = TextNormalizer.normalize(book.title).contains(normalizedQuery)
            val matchesAuthor = book.author
                ?.let { TextNormalizer.normalize(it).contains(normalizedQuery) }
                ?: false
            val matchesAnnotation = annotationsByBook[book.id]
                ?.any { TextNormalizer.normalize(it.text).contains(normalizedQuery) }
                ?: false

            matchesTitle || matchesAuthor || matchesAnnotation
        }
    }
}