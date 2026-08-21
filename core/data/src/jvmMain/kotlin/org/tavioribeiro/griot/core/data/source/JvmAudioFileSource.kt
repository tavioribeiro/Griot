package org.tavioribeiro.griot.core.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tavioribeiro.griot.core.domain.book.model.AudioFileCandidate
import org.tavioribeiro.griot.core.domain.book.source.AudioFileSource
import java.io.File
import javax.sound.sampled.AudioSystem

class JvmAudioFileSource : AudioFileSource {

    override suspend fun folderExists(folderPath: String): Boolean = withContext(Dispatchers.IO) {
        File(folderPath).isDirectory
    }

    override suspend fun listRootFiles(folderPath: String): List<AudioFileCandidate> =
        withContext(Dispatchers.IO) {
            File(folderPath)
                .listFiles { file -> file.isFile && file.isAudioFile() }
                ?.sortedBy { it.name }
                ?.map { AudioFileCandidate(it.name, it.absolutePath) }
                .orEmpty()
        }

    override suspend fun readDurationMs(filePath: String): Long = withContext(Dispatchers.IO) {
        try {
            val format = AudioSystem.getAudioFileFormat(File(filePath))
            val frameLength = format.frameLength.toLong()
            val frameRate = format.format.frameRate
            if (frameLength > 0L && frameRate > 0f) (frameLength * 1000L / frameRate).toLong() else 0L
        } catch (t: Throwable) {
            0L
        }
    }

    private fun File.isAudioFile(): Boolean =
        name.substringAfterLast('.', "").lowercase() in AUDIO_EXTENSIONS

    companion object {
        private val AUDIO_EXTENSIONS = setOf(
            "mp3", "m4b", "m4a", "aac", "ogg", "oga", "opus", "wav", "flac", "wma", "amr", "aiff", "aif"
        )
    }
}