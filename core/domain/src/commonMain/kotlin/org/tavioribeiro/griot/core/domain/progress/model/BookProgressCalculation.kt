package org.tavioribeiro.griot.core.domain.progress.model

data class BookProgressCalculation(
    val percentage: Float,             // Ex: 42.5f (0 a 100)
    val totalBookDurationMs: Long,     // Duração somada de todas as faixas
    val totalTimeListenedMs: Long,     // Tempo percorrido no livro inteiro
    val isNearCompletion: Boolean      // RN-CNC-001: true se >= 97%
)