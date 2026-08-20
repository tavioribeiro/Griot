package org.tavioribeiro.griot.core.domain.book.model

data class BookImportResult(
    val book: Book,
    val tracks: List<AudioTrack>,
    val sequenceType: TrackSequenceType
)