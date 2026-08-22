package org.tavioribeiro.griot.core.data.annotation.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tavioribeiro.griot.core.data.db.Annotation_
import org.tavioribeiro.griot.core.data.db.GriotDatabase
import org.tavioribeiro.griot.core.domain.annotation.model.Annotation
import org.tavioribeiro.griot.core.domain.annotation.model.AnnotationId
import org.tavioribeiro.griot.core.domain.annotation.repository.AnnotationRepository
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.book.model.TrackId
import org.tavioribeiro.griot.core.domain.error.AnnotationError
import org.tavioribeiro.griot.core.domain.error.DomainResult
import org.tavioribeiro.griot.core.domain.shelf.util.TextNormalizer

class SqlDelightAnnotationRepository(private val database: GriotDatabase) : AnnotationRepository {

    override fun observeAnnotationsByBookId(bookId: BookId): Flow<List<Annotation>> =
        database.griotQueries.selectAnnotationsByBook(bookId.value)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.toDomain() } }

    override suspend fun getAnnotationById(id: AnnotationId): DomainResult<Annotation, AnnotationError> =
        try {
            val row = database.griotQueries.selectAnnotationById(id.value).executeAsOneOrNull()
            if (row == null) DomainResult.Error(AnnotationError.AnnotationNotFound)
            else DomainResult.Success(row.toDomain())
        } catch (t: Throwable) {
            DomainResult.Error(AnnotationError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun createAnnotation(annotation: Annotation): DomainResult<Unit, AnnotationError> =
        try {
            database.griotQueries.insertAnnotation(
                id = annotation.id.value,
                bookId = annotation.bookId.value,
                trackId = annotation.trackId.value,
                timestampMs = annotation.timestampMs,
                text = annotation.text,
                createdAtEpochMs = annotation.createdAtEpochMs,
                updatedAtEpochMs = annotation.updatedAtEpochMs
            )
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(AnnotationError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun updateAnnotationText(
        id: AnnotationId,
        newText: String,
        updatedAtEpochMs: Long
    ): DomainResult<Unit, AnnotationError> =
        try {
            val affected = database.griotQueries.updateAnnotationText(
                text = newText,
                updatedAtEpochMs = updatedAtEpochMs,
                id = id.value
            ).value
            if (affected == 0L) DomainResult.Error(AnnotationError.AnnotationNotFound)
            else DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(AnnotationError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun deleteAnnotation(id: AnnotationId): DomainResult<Unit, AnnotationError> =
        try {
            val affected = database.griotQueries.deleteAnnotation(id = id.value).value
            if (affected == 0L) DomainResult.Error(AnnotationError.AnnotationNotFound)
            else DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(AnnotationError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun deleteAnnotationsByBookId(bookId: BookId): DomainResult<Unit, AnnotationError> =
        try {
            database.griotQueries.deleteAnnotationsByBook(bookId = bookId.value)
            DomainResult.Success(Unit)
        } catch (t: Throwable) {
            DomainResult.Error(AnnotationError.StorageError(t.message ?: "DB error", t))
        }

    override suspend fun searchAnnotations(query: String): DomainResult<List<Annotation>, AnnotationError> =
        try {
            val normalizedQuery = TextNormalizer.normalize(query)
            val rows = database.griotQueries.selectAllAnnotations().executeAsList()
            val matches = if (normalizedQuery.isBlank()) {
                rows
            } else {
                rows.filter { TextNormalizer.normalize(it.text).contains(normalizedQuery) }
            }
            DomainResult.Success(matches.map { it.toDomain() })
        } catch (t: Throwable) {
            DomainResult.Error(AnnotationError.StorageError(t.message ?: "DB error", t))
        }

    private fun Annotation_.toDomain(): Annotation =
        Annotation(
            id = AnnotationId(id),
            bookId = BookId(bookId),
            trackId = TrackId(trackId),
            timestampMs = timestampMs,
            text = text,
            createdAtEpochMs = createdAtEpochMs,
            updatedAtEpochMs = updatedAtEpochMs
        )
}
