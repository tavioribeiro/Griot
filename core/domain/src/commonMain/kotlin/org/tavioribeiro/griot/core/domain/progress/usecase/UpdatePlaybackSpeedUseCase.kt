package org.tavioribeiro.griot.core.domain.progress.usecase

import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.error.ProgressError
import org.tavioribeiro.griot.core.domain.progress.model.PlaybackSpeed
import org.tavioribeiro.griot.core.domain.progress.repository.ProgressRepository

class UpdatePlaybackSpeedUseCase(
    private val progressRepository: ProgressRepository
) {

    suspend operator fun invoke(
        bookId: BookId,
        speed: PlaybackSpeed
    ): DomainResult<Unit, ProgressError> {
        return when (val result = progressRepository.getProgressByBookId(bookId)) {
            is DomainResult.Error -> result
            is DomainResult.Success -> {
                val updated = result.data.copy(playbackSpeed = speed.value)
                progressRepository.saveProgress(updated)
            }
        }
    }
}