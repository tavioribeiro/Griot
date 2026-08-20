package org.tavioribeiro.griot.core.domain.progress.model

import kotlin.math.abs
import kotlin.math.roundToInt

@JvmInline
value class PlaybackSpeed(val value: Float) {

    init {
        require(isValid(value)) {
            "Velocidade inválida: $value. Use um valor entre $MIN_VALUE e $MAX_VALUE em incrementos de $STEP."
        }
    }

    companion object {
        const val MIN_VALUE = 0.5f
        const val MAX_VALUE = 3.0f
        const val STEP = 0.25f

        val ALLOWED: List<PlaybackSpeed> = (2..12).map { PlaybackSpeed(it * STEP) }

        val DEFAULT: PlaybackSpeed = PlaybackSpeed(1.0f)

        fun isValid(value: Float): Boolean {
            if (value < MIN_VALUE || value > MAX_VALUE) return false
            val steps = (value - MIN_VALUE) / STEP
            return abs(steps - steps.roundToInt()) < 0.001f
        }
    }
}