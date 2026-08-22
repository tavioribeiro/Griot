package org.tavioribeiro.griot.core.data.book.source

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tavioribeiro.griot.core.domain.book.model.AudioFileCandidate
import org.tavioribeiro.griot.core.domain.book.source.AudioFileSource
import java.io.File

class AndroidAudioFileSource(private val context: Context) : AudioFileSource {

    override suspend fun folderExists(folderPath: String): Boolean = withContext(Dispatchers.IO) {
        val uri = Uri.parse(folderPath)
        if (uri.isContentUri()) {
            DocumentFile.fromTreeUri(context, uri)?.exists() ?: false
        } else {
            File(folderPath).isDirectory
        }
    }

    override suspend fun listRootFiles(folderPath: String): List<AudioFileCandidate> =
        withContext(Dispatchers.IO) {
            val uri = Uri.parse(folderPath)
            if (uri.isContentUri()) {
                DocumentFile.fromTreeUri(context, uri)
                    ?.listFiles()
                    ?.filter { it.isFile && it.isAudioFile() }
                    ?.sortedBy { it.name }
                    ?.map { AudioFileCandidate(it.name ?: it.uri.toString(), it.uri.toString()) }
                    .orEmpty()
            } else {
                File(folderPath)
                    .listFiles { file -> file.isFile && file.isAudioFile() }
                    ?.sortedBy { it.name }
                    ?.map { AudioFileCandidate(it.name, it.absolutePath) }
                    .orEmpty()
            }
        }

    override suspend fun readDurationMs(filePath: String): Long = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        try {
            val uri = Uri.parse(filePath)
            if (uri.isContentUri()) {
                retriever.setDataSource(context, uri)
            } else {
                retriever.setDataSource(filePath)
            }
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
        } catch (t: Throwable) {
            0L
        } finally {
            runCatching { retriever.release() }
        }
    }

    private fun Uri.isContentUri(): Boolean = scheme == "content"

    private fun DocumentFile.isAudioFile(): Boolean =
        (name ?: "").substringAfterLast('.', "").lowercase() in AUDIO_EXTENSIONS

    private fun File.isAudioFile(): Boolean =
        name.substringAfterLast('.', "").lowercase() in AUDIO_EXTENSIONS

    companion object {
        private val AUDIO_EXTENSIONS = setOf(
            "mp3", "m4b", "m4a", "aac", "ogg", "oga", "opus", "wav", "flac", "wma", "amr", "aiff", "aif"
        )
    }
}
