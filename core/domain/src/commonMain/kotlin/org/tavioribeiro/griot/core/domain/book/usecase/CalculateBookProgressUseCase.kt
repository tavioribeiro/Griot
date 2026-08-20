package org.tavioribeiro.griot.core.domain.book.usecase

import org.tavioribeiro.griot.core.domain.book.model.AudioTrack
import org.tavioribeiro.griot.core.domain.book.model.TrackId
import org.tavioribeiro.griot.core.domain.progress.model.BookProgressCalculation
import org.tavioribeiro.griot.core.domain.progress.model.PlaybackConstants

class CalculateBookProgressUseCase {

    operator fun invoke(
        tracks: List<AudioTrack>,
        currentTrackId: TrackId,
        currentPositionMs: Long
    ): BookProgressCalculation {
        if (tracks.isEmpty()) {
            return BookProgressCalculation(
                percentage = 0f,
                totalBookDurationMs = 0L,
                totalTimeListenedMs = 0L,
                isNearCompletion = false
            )
        }

        val sortedTracks = tracks.sortedBy { it.orderIndex }
        val totalDuration = sortedTracks.sumOf { it.durationMs }

        if (totalDuration == 0L) {
            return BookProgressCalculation(
                percentage = 0f,
                totalBookDurationMs = 0L,
                totalTimeListenedMs = 0L,
                isNearCompletion = false
            )
        }

        val currentTrackIndex = sortedTracks.indexOfFirst { it.id == currentTrackId }
        if (currentTrackIndex == -1) {
            return BookProgressCalculation(
                percentage = 0f,
                totalBookDurationMs = totalDuration,
                totalTimeListenedMs = 0L,
                isNearCompletion = false
            )
        }

        val previousTracksDuration = sortedTracks
            .take(currentTrackIndex)
            .sumOf { it.durationMs }

        val safeCurrentPosition = currentPositionMs.coerceIn(0L, sortedTracks[currentTrackIndex].durationMs)
        val timeListened = (previousTracksDuration + safeCurrentPosition).coerceAtMost(totalDuration)

        val percentage = ((timeListened.toDouble() / totalDuration.toDouble()) * 100.0).toFloat()
            .coerceIn(0f, 100f)

        return BookProgressCalculation(
            percentage = percentage,
            totalBookDurationMs = totalDuration,
            totalTimeListenedMs = timeListened,
            isNearCompletion = percentage >= PlaybackConstants.NEAR_COMPLETION_THRESHOLD
        )
    }
}
