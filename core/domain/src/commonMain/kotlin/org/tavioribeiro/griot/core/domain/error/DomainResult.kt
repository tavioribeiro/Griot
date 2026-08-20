package org.tavioribeiro.griot.core.domain.error



sealed interface DomainResult<out D, out E> {
    data class Success<out D>(val data: D) : DomainResult<D, Nothing>
    data class Error<out E>(val error: E) : DomainResult<Nothing, E>
}

sealed interface RootError


sealed interface BookError : RootError {
    data object EmptyText : BookError
    data object FolderNotFound : BookError
    data object NoAudioFilesFound : BookError
    data object DuplicateSourcePath : BookError // RN-LIV-005: Pasta já em uso
    data object BookNotFound : BookError
    data class StorageError(val message: String, val cause: Throwable? = null) : BookError
    data class Unknown(val cause: Throwable? = null) : BookError
}

sealed interface AnnotationError : RootError {
    data object EmptyText : AnnotationError // RN-NOT-003: Texto em branco
    data object AnnotationNotFound : AnnotationError
    data object InvalidTimestamp : AnnotationError // RN-NOT-001: timestamp não pode ser negativo
    data class StorageError(val message: String, val cause: Throwable? = null) : AnnotationError
    data class Unknown(val cause: Throwable? = null) : AnnotationError
}

sealed interface TrackError : RootError {
    data object TrackNotFound : TrackError
    data object EmptyTrackList : TrackError
    data class StorageError(val message: String, val cause: Throwable? = null) : TrackError
    data class Unknown(val cause: Throwable? = null) : TrackError
}

sealed interface ProgressError : RootError {
    data object ProgressNotFound : ProgressError
    data class StorageError(val message: String, val cause: Throwable? = null) : ProgressError
    data class Unknown(val cause: Throwable? = null) : ProgressError
}

sealed interface ImportError : RootError {
    data object FolderNotFound : ImportError // RN-ARQ-002: pasta inexistente
    data object NoAudioFilesFound : ImportError // RN-ARQ-003: mínimo 1 arquivo válido
    data object DuplicateSourcePath : ImportError // RN-LIV-005: pasta já vinculada a um livro
    data object InvalidTrackOrder : ImportError // RN-FAI-003: override deve ser permutação dos arquivos
    data class StorageError(val message: String, val cause: Throwable? = null) : ImportError
    data class Unknown(val cause: Throwable? = null) : ImportError
}

sealed interface RemovalError : RootError {
    data object BookNotFound : RemovalError
    data class StorageError(val message: String, val cause: Throwable? = null) : RemovalError
    data class Unknown(val cause: Throwable? = null) : RemovalError
}