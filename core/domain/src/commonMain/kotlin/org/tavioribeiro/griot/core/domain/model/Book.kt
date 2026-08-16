package org.tavioribeiro.griot.core.domain.model


enum class BookStatus{
    AVAILABLE,
    SOURCE_UNAVAILABLE
}

data class Book(
    val id: BookId,
    val title: String,
    val author: String? = null,
    val coverUri: String? = null,
    val sourceFolderPath: String,
    val status: BookStatus = BookStatus.AVAILABLE,
    val createdAtEpochMs: Long
)