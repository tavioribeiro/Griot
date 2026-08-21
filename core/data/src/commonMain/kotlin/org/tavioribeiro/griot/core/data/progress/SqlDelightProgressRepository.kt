package org.tavioribeiro.griot.core.data.progress

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tavioribeiro.griot.core.data.db.GriotDatabase
import org.tavioribeiro.griot.core.data.db.Progress as ProgressRow
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.TrackId
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.error.ProgressError
import org.tavioribeiro.griot.core.domain.progress.model.PlaybackProgress
import org.tavioribeiro.griot.core.domain.progress.repository.ProgressRepository

class SqlDelightProgressRepository(private val database: GriotDatabase) : ProgressRepository {

    override fun observeProgressByBookId(bookId: BookId): Flow<PlaybackProgress?> =
        database.griotQueries.selectProgressByBook(bookId.value)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }

    override suspend fun getProgressByBookId(bookId: BookId): DomainResult<PlaybackProgress, ProgressError> =
        try {
            val row = database.griotQueries.selectProgressByBook(bookId.value).executeAsOneOrNull()
            if (row == null) DomainResult.Error(ProgressError.ProgressNotFound)
            else DomainResult.Success(row.toDomain())
        } catch (t: Throwable) {
            DomainResult.Error(ProgressError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun saveProgress(progress: PlaybackProgress): DomainResult<Unit, ProgressError> =
        try {
            database.griotQueries.insertOrReplaceProgress(
                bookId = progress.bookId.value,
                currentTrackId = progress.currentTrackId.value,
                currentPositionMs = progress.currentPositionMs,
                playbackSpeed = progress.playbackSpeed.toDouble(),
                isCompleted = if (progress.isCompleted) 1L else 0L,
                lastPlayedAtEpochMs = progress.lastPlayedAtEpochMs
            )
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(ProgressError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun updateCompletionStatus(
        bookId: BookId,
        isCompleted: Boolean
    ): DomainResult<Unit, ProgressError> =
        try {
            database.griotQueries.updateCompletionStatus(
                isCompleted = if (isCompleted) 1L else 0L,
                bookId = bookId.value
            )
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(ProgressError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun deleteProgressByBookId(bookId: BookId): DomainResult<Unit, ProgressError> =
        try {
            database.griotQueries.deleteProgressByBook(bookId = bookId.value)
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(ProgressError.StorageError(t.message ?: "DB error", t))
        }

    private fun ProgressRow.toDomain(): PlaybackProgress =
        PlaybackProgress(
            bookId = BookId(bookId),
            currentTrackId = TrackId(currentTrackId),
            currentPositionMs = currentPositionMs,
            playbackSpeed = playbackSpeed.toFloat(),
            isCompleted = isCompleted == 1L,
            lastPlayedAtEpochMs = lastPlayedAtEpochMs
        )
}