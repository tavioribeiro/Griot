package org.tavioribeiro.griot.core.domain.book.model

data class BookImportRequest(
    val sourceFolderPath: String,
    val title: String? = null,
    val author: String? = null,
    val coverUri: String? = null,
    val trackOrderOverride: List<String>? = null
)