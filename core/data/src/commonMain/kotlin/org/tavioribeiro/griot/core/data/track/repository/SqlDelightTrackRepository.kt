package org.tavioribeiro.griot.core.data.track.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tavioribeiro.griot.core.data.db.GriotDatabase
import org.tavioribeiro.griot.core.data.db.Track as TrackRow
import org.tavioribeiro.griot.core.domain.book.model.AudioTrack
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.TrackId
import org.tavioribeiro.griot.core.domain.book.repository.TrackRepository
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.error.TrackError

class SqlDelightTrackRepository(private val database: GriotDatabase) : TrackRepository {

    override fun observeTracksByBookId(bookId: BookId): Flow<List<AudioTrack>> =
        database.griotQueries.selectTracksByBook(bookId.value)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.toDomain() } }

    override suspend fun getTracksByBookId(bookId: BookId): DomainResult<List<AudioTrack>, TrackError> =
        try {
            val rows = database.griotQueries.selectTracksByBook(bookId.value).executeAsList()
            if (rows.isEmpty()) DomainResult.Error(TrackError.EmptyTrackList)
            else DomainResult.Success(rows.map { it.toDomain() })
        } catch (t: Throwable) {
            DomainResult.Error(TrackError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun getTrackById(trackId: TrackId): DomainResult<AudioTrack, TrackError> =
        try {
            val row = database.griotQueries.selectTrackById(trackId.value).executeAsOneOrNull()
            if (row == null) DomainResult.Error(TrackError.TrackNotFound)
            else DomainResult.Success(row.toDomain())
        } catch (t: Throwable) {
            DomainResult.Error(TrackError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun insertTracks(tracks: List<AudioTrack>): DomainResult<Unit, TrackError> =
        try {
            database.transaction {
                tracks.forEach { track ->
                    database.griotQueries.insertTrack(
                        id = track.id.value,
                        bookId = track.bookId.value,
                        title = track.title,
                        filePath = track.filePath,
                        orderIndex = track.orderIndex.toLong(),
                        durationMs = track.durationMs
                    )
                }
            }
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(TrackError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun deleteTracksByBookId(bookId: BookId): DomainResult<Unit, TrackError> =
        try {
            database.griotQueries.deleteTracksByBook(bookId = bookId.value)
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(TrackError.StorageError(t.message ?: "DB error", t))
        }

    private fun TrackRow.toDomain(): AudioTrack =
        AudioTrack(
            id = TrackId(id),
            bookId = BookId(bookId),
            title = title,
            filePath = filePath,
            orderIndex = orderIndex.toInt(),
            durationMs = durationMs
        )
}
