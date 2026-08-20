package org.tavioribeiro.griot.core.domain.progress.usecase

import org.tavioribeiro.griot.core.domain.progress.model.PlaybackConstants

enum class SkipDirection {
    FORWARD,
    BACKWARD
}

class CalculateSkippedPositionUseCase {

    operator fun invoke(
        currentPositionMs: Long,
        trackDurationMs: Long,
        direction: SkipDirection
    ): Long {
        val targetMs = when (direction) {
            SkipDirection.FORWARD -> currentPositionMs + PlaybackConstants.SKIP_AMOUNT_MS
            SkipDirection.BACKWARD -> currentPositionMs - PlaybackConstants.SKIP_AMOUNT_MS
        }
        return targetMs.coerceIn(0L, trackDurationMs.coerceAtLeast(0L))
    }
}