package org.tavioribeiro.griot.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.error.ProgressError
import org.tavioribeiro.griot.core.domain.model.BookId
import org.tavioribeiro.griot.core.domain.model.PlaybackProgress


interface ProgressRepository {
    fun observeProgressByBookId(bookId: BookId): Flow<PlaybackProgress?>
    suspend fun getProgressByBookId(bookId: BookId): DomainResult<PlaybackProgress, ProgressError>
    suspend fun saveProgress(progress: PlaybackProgress): DomainResult<Unit, ProgressError>
    suspend fun updateCompletionStatus(bookId: BookId, isCompleted: Boolean): DomainResult<Unit, ProgressError>
}