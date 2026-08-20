package org.tavioribeiro.griot.core.domain.shelf.usecase

import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.progress.model.PlaybackProgress
import org.tavioribeiro.griot.core.domain.shelf.model.BookFilter
import org.tavioribeiro.griot.core.domain.shelf.model.ShelfItem

class GetShelfUseCase {

    operator fun invoke(
        books: List<Book>,
        progressByBook: Map<BookId, PlaybackProgress>,
        filter: BookFilter = BookFilter.ALL
    ): List<ShelfItem> {
        val items = books
            .map { ShelfItem(book = it, progress = progressByBook[it.id]) }
            .filter { matchesFilter(it, filter) }

        return items.sortedWith(
            compareByDescending<ShelfItem> { it.lastPlayedAtEpochMs ?: Long.MIN_VALUE }
                .thenByDescending { it.book.createdAtEpochMs }
        )
    }

    private fun matchesFilter(item: ShelfItem, filter: BookFilter): Boolean = when (filter) {
        BookFilter.ALL -> true
        BookFilter.IN_PROGRESS -> item.isSourceAvailable && item.hasProgress && !item.isCompleted
        BookFilter.COMPLETED -> item.isCompleted
        BookFilter.SOURCE_UNAVAILABLE -> !item.isSourceAvailable
    }
}