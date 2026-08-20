package org.tavioribeiro.griot.core.domain.book.usecase

import org.tavioribeiro.griot.core.domain.book.model.AudioFileCandidate
import org.tavioribeiro.griot.core.domain.book.model.AudioTrack
import org.tavioribeiro.griot.core.domain.book.model.Book
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.BookImportRequest
import org.tavioribeiro.griot.core.domain.book.model.BookImportResult
import org.tavioribeiro.griot.core.domain.book.model.TrackId
import org.tavioribeiro.griot.core.domain.book.model.TrackSequenceType
import org.tavioribeiro.griot.core.domain.book.repository.BookRepository
import org.tavioribeiro.griot.core.domain.book.repository.TrackRepository
import org.tavioribeiro.griot.core.domain.book.source.AudioFileSource
import org.tavioribeiro.griot.core.domain.error.BookError
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.error.ImportError
import org.tavioribeiro.griot.core.domain.error.RootError
import org.tavioribeiro.griot.core.domain.error.TrackError
import org.tavioribeiro.griot.core.domain.util.NaturalOrderComparator
import org.tavioribeiro.griot.core.domain.util.RandomIdGenerator

class ImportBookUseCase(
    private val bookRepository: BookRepository,
    private val trackRepository: TrackRepository,
    private val audioFileSource: AudioFileSource,
    private val newId: () -> String = { RandomIdGenerator.next() },
    private val nowEpochMs: () -> Long
) {

    suspend operator fun invoke(request: BookImportRequest): DomainResult<BookImportResult, ImportError> {
        return try {
            // RN-ARQ-002: a pasta de origem deve existir
            if (!audioFileSource.folderExists(request.sourceFolderPath)) {
                return DomainResult.Error(ImportError.FolderNotFound)
            }

            // RN-LIV-005: uma pasta só pode estar vinculada a um único livro
            when (val inUse = bookRepository.isSourcePathInUse(request.sourceFolderPath)) {
                is DomainResult.Error -> return toImportError(inUse.error)
                is DomainResult.Success -> if (inUse.data) {
                    return DomainResult.Error(ImportError.DuplicateSourcePath)
                }
            }

            // RN-ARQ-002: apenas raiz; RN-ARQ-001: apenas formatos suportados
            val rootFiles = audioFileSource.listRootFiles(request.sourceFolderPath)
            val validFiles = rootFiles.filter { AudioFileValidation.isSupportedFileName(it.fileName) }

            // RN-ARQ-003: mínimo de 1 arquivo válido
            if (validFiles.isEmpty()) {
                return DomainResult.Error(ImportError.NoAudioFilesFound)
            }

            // RN-ARQ-004: classificação (1 arquivo = faixa única; >1 = múltiplos)
            val sequenceType = if (validFiles.size == 1) {
                TrackSequenceType.SINGLE_TRACK
            } else {
                TrackSequenceType.MULTIPLE_TRACKS
            }

            // RN-FAI-002/003: ordenação natural inicial ou override do usuário
            val orderedFiles = when (val result = resolveOrder(validFiles, request.trackOrderOverride)) {
                is DomainResult.Error -> return result
                is DomainResult.Success -> result.data
            }

            // RN-LIV-001/002/003: identidade + metadados do livro
            val bookId = BookId(newId())
            val book = Book(
                id = bookId,
                title = resolveTitle(request.title, request.sourceFolderPath),
                author = request.author?.trim()?.takeIf { it.isNotEmpty() },
                coverUri = request.coverUri?.trim()?.takeIf { it.isNotEmpty() },
                sourceFolderPath = request.sourceFolderPath,
                createdAtEpochMs = nowEpochMs()
            )

            // RN-FAI-001/005: faixas com identidade própria e duração lida
            val tracks = orderedFiles.mapIndexed { index, file ->
                AudioTrack(
                    id = TrackId(newId()),
                    bookId = bookId,
                    title = displayTitle(file.fileName),
                    filePath = file.filePath,
                    orderIndex = index,
                    durationMs = audioFileSource.readDurationMs(file.filePath)
                )
            }

            when (val result = bookRepository.insertBook(book)) {
                is DomainResult.Error -> return toImportError(result.error)
                is DomainResult.Success -> Unit
            }
            when (val result = trackRepository.insertTracks(tracks)) {
                is DomainResult.Error -> return toImportError(result.error)
                is DomainResult.Success -> Unit
            }

            DomainResult.Success(BookImportResult(book = book, tracks = tracks, sequenceType = sequenceType))
        } catch (e: Exception) {
            DomainResult.Error(ImportError.Unknown(e))
        }
    }

    private fun resolveOrder(
        files: List<AudioFileCandidate>,
        override: List<String>?
    ): DomainResult<List<AudioFileCandidate>, ImportError> {
        if (override == null) {
            val sorted = files.sortedWith(compareBy(NaturalOrderComparator) { it.fileName })
            return DomainResult.Success(sorted)
        }

        val foundPaths = files.map { it.filePath }.toSet()
        val overrideSet = override.toSet()
        val isValidPermutation = overrideSet == foundPaths && override.size == foundPaths.size
        if (!isValidPermutation) {
            return DomainResult.Error(ImportError.InvalidTrackOrder)
        }

        val ordered = files.sortedBy { override.indexOf(it.filePath) }
        return DomainResult.Success(ordered)
    }

    private fun resolveTitle(title: String?, folderPath: String): String {
        title?.trim()?.takeIf { it.isNotEmpty() }?.let { return it }

        val folderName = folderPath
            .trimEnd('/', '\\')
            .substringAfterLast('/')
            .substringAfterLast('\\')

        return folderName
            .replace('_', ' ')
            .replace('-', ' ')
            .replace(Regex("\\s+"), " ")
            .trim()
            .ifEmpty { folderPath }
    }

    private fun displayTitle(fileName: String): String {
        val withoutExtension = fileName.substringBeforeLast('.')
        return withoutExtension.ifBlank { fileName }
    }

    private fun toImportError(error: RootError): DomainResult<Nothing, ImportError> =
        when (error) {
            is BookError.DuplicateSourcePath -> DomainResult.Error(ImportError.DuplicateSourcePath)
            is BookError.StorageError -> DomainResult.Error(ImportError.StorageError(error.message, error.cause))
            is TrackError.StorageError -> DomainResult.Error(ImportError.StorageError(error.message, error.cause))
            else -> DomainResult.Error(ImportError.Unknown())
        }
}