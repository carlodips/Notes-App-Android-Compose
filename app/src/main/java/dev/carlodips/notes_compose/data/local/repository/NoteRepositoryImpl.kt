package dev.carlodips.notes_compose.data.local.repository

import dev.carlodips.notes_compose.data.local.dao.NoteDao
import dev.carlodips.notes_compose.data.local.entity.NoteEntity
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override suspend fun insertNote(note: Note): Long {
        return dao.insertNote(NoteEntity.toEntity(note))
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(NoteEntity.toEntity(note))
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)?.toDomain()
    }

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes().map {list ->
            list.map { noteEntity ->
                noteEntity.toDomain()
            }
        }
    }

    override suspend fun setArchivedNote(noteId: Int, isArchived: Boolean) {
        dao.setArchivedNote(noteId, isArchived)
    }

    override suspend fun setLockedNote(noteId: Int, isLocked: Boolean) {
        dao.setLockedNote(noteId, isLocked)
    }
}
