package org.tavioribeiro.griot.core.domain.book.source

import org.tavioribeiro.griot.core.domain.book.model.AudioFileCandidate


interface AudioFileSource {

    suspend fun folderExists(folderPath: String): Boolean

    suspend fun listRootFiles(folderPath: String): List<AudioFileCandidate>

    //RN-FAI-005
    suspend fun readDurationMs(filePath: String): Long
}