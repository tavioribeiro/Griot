package org.tavioribeiro.griot.core.domain.progress.usecase

import org.tavioribeiro.griot.core.domain.book.model.TrackId
import org.tavioribeiro.griot.core.domain.progress.model.PlaybackProgress


class ResetProgressOnTrackChangeUseCase {

    operator fun invoke(
        current: PlaybackProgress,
        targetTrackId: TrackId,
        newPositionMs: Long = 0L,
        nowEpochMs: Long
    ): PlaybackProgress {
        return current.copy(
            currentTrackId = targetTrackId,
            currentPositionMs = newPositionMs.coerceAtLeast(0L),
            lastPlayedAtEpochMs = nowEpochMs
        )
    }
}