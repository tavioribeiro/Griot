package org.tavioribeiro.griot.core.domain.shelf.model

import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.model.BookStatus
import org.tavioribeiro.griot.core.domain.progress.model.PlaybackProgress

data class ShelfItem(
    val book: Book,
    val progress: PlaybackProgress?
) {
    val lastPlayedAtEpochMs: Long?
        get() = progress?.lastPlayedAtEpochMs

    val isCompleted: Boolean
        get() = progress?.isCompleted ?: false

    val hasProgress: Boolean
        get() = progress != null

    val isSourceAvailable: Boolean
        get() = book.status == BookStatus.AVAILABLE
}