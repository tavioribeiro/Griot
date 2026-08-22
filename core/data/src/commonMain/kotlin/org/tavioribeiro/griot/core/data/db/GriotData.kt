package org.tavioribeiro.griot.core.data.db

import org.tavioribeiro.griot.core.data.annotation.repository.SqlDelightAnnotationRepository
import org.tavioribeiro.griot.core.data.book.repository.SqlDelightBookRepository
import org.tavioribeiro.griot.core.data.db.driver.DatabaseDriverFactory
import org.tavioribeiro.griot.core.data.progress.repository.SqlDelightProgressRepository
import org.tavioribeiro.griot.core.data.track.repository.SqlDelightTrackRepository
import org.tavioribeiro.griot.core.domain.annotation.repository.AnnotationRepository
import org.tavioribeiro.griot.core.domain.book.repository.BookRepository
import org.tavioribeiro.griot.core.domain.book.repository.TrackRepository
import org.tavioribeiro.griot.core.domain.progress.repository.ProgressRepository

class GriotData(driverFactory: DatabaseDriverFactory) {

    private val database = GriotDatabase(driverFactory.createDriver())

    val bookRepository: BookRepository = SqlDelightBookRepository(database)
    val trackRepository: TrackRepository = SqlDelightTrackRepository(database)
    val annotationRepository: AnnotationRepository = SqlDelightAnnotationRepository(database)
    val progressRepository: ProgressRepository = SqlDelightProgressRepository(database)
}