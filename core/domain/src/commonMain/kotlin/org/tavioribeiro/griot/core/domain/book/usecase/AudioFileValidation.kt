package org.tavioribeiro.griot.core.domain.book.usecase

object AudioFileValidation {

    val SUPPORTED_EXTENSIONS: Set<String> = setOf("mp3", "wav", "flac")

    fun isSupportedFileName(fileName: String): Boolean {
        val extension = fileName.substringAfterLast('.', missingDelimiterValue = "").lowercase()
        return extension in SUPPORTED_EXTENSIONS
    }
}