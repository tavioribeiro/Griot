package org.tavioribeiro.griot.core.domain.annotation.repository

import kotlinx.coroutines.flow.Flow
import org.tavioribeiro.griot.core.domain.annotation.model.Annotation
import org.tavioribeiro.griot.core.domain.annotation.model.AnnotationId
import org.tavioribeiro.griot.core.domain.book.model.BookId
import org.tavioribeiro.griot.core.domain.error.AnnotationError
import org.tavioribeiro.griot.core.domain.error.DomainResult


interface AnnotationRepository {
    fun observeAnnotationsByBookId(bookId: BookId): Flow<List<Annotation>>
    suspend fun getAnnotationById(id: AnnotationId): DomainResult<Annotation, AnnotationError>
    suspend fun createAnnotation(annotation: Annotation): DomainResult<Unit, AnnotationError>
    suspend fun updateAnnotationText(
        id: AnnotationId,
        newText: String,
        updatedAtEpochMs: Long
    ): DomainResult<Unit, AnnotationError>
    suspend fun deleteAnnotation(id: AnnotationId): DomainResult<Unit, AnnotationError>
    suspend fun deleteAnnotationsByBookId(bookId: BookId): DomainResult<Unit, AnnotationError>
    suspend fun searchAnnotations(query: String): DomainResult<List<Annotation>, AnnotationError>
}