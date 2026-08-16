package org.tavioribeiro.griot.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.error.TrackError
import org.tavioribeiro.griot.core.domain.model.AudioTrack
import org.tavioribeiro.griot.core.domain.model.BookId
import org.tavioribeiro.griot.core.domain.model.TrackId


interface TrackRepository {
    fun observeTracksByBookId(bookId: BookId): Flow<List<AudioTrack>>
    suspend fun getTracksByBookId(bookId: BookId): DomainResult<List<AudioTrack>, TrackError>
    suspend fun getTrackById(trackId: TrackId): DomainResult<AudioTrack, TrackError>
    suspend fun insertTracks(tracks: List<AudioTrack>): DomainResult<Unit, TrackError>
}
