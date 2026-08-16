package org.tavioribeiro.griot.core.domain.book.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.tavioribeiro.griot.core.domain.book.model.AudioTrack
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.TrackId

class CalculateBookProgressUseCaseTest {

    private val useCase = CalculateBookProgressUseCase()
    private val sampleBookId = BookId("book-123")

    private val track1 = AudioTrack(
        id = TrackId("t1"),
        bookId = sampleBookId,
        title = "Faixa 1",
        filePath = "/path/01.mp3",
        orderIndex = 0,
        durationMs = 60_000L // 1 minuto
    )

    private val track2 = AudioTrack(
        id = TrackId("t2"),
        bookId = sampleBookId,
        title = "Faixa 2",
        filePath = "/path/02.mp3",
        orderIndex = 1,
        durationMs = 60_000L // 1 minuto
    )

    @Test
    fun `quando nao ha faixas, retorna progresso zero`() {
        val result = useCase(
            tracks = emptyList(),
            currentTrackId = TrackId("none"),
            currentPositionMs = 0L
        )

        assertEquals(0f, result.percentage)
        assertEquals(0L, result.totalBookDurationMs)
        assertEquals(0L, result.totalTimeListenedMs)
        assertFalse(result.isNearCompletion)
    }

    @Test
    fun `quando esta na metade da primeira faixa de duas, calcula 25 porcento`() {
        // Total = 120_000ms. Posição = 30_000ms da Faixa 1 (30s de 120s = 25%)
        val result = useCase(
            tracks = listOf(track1, track2),
            currentTrackId = track1.id,
            currentPositionMs = 30_000L
        )

        assertEquals(25f, result.percentage)
        assertEquals(120_000L, result.totalBookDurationMs)
        assertEquals(30_000L, result.totalTimeListenedMs)
        assertFalse(result.isNearCompletion)
    }

    @Test
    fun `quando esta na metade da segunda faixa, calcula 75 porcento`() {
        // Total = 120_000ms. Faixa 1 (60s) + Faixa 2 (30s) = 90s de 120s = 75%
        val result = useCase(
            tracks = listOf(track1, track2),
            currentTrackId = track2.id,
            currentPositionMs = 30_000L
        )

        assertEquals(75f, result.percentage)
        assertEquals(90_000L, result.totalTimeListenedMs)
        assertFalse(result.isNearCompletion)
    }

    @Test
    fun `quando atinge 97 porcento ou mais, ativa flag isNearCompletion`() {
        // Total = 120_000ms. 97% de 120_000ms = 116_400ms.
        // Faixa 1 (60_000ms) + Faixa 2 (57_000ms) = 117_000ms (97.5%)
        val result = useCase(
            tracks = listOf(track1, track2),
            currentTrackId = track2.id,
            currentPositionMs = 57_000L
        )

        assertTrue(result.percentage >= 97.0f)
        assertTrue(result.isNearCompletion)
    }
}
