package dev.carlodips.notes_compose.data.local.repository

import dev.carlodips.notes_compose.data.local.dao.NotesDao
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(
    private val dao: NotesDao
) : NoteRepository {
    override suspend fun insertNote(note: Note): Long {
        return dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun setHiddenNote(noteId: Int, isHidden: Boolean) {
        dao.setHiddenNote(noteId, isHidden)
    }

    override suspend fun setLockedNote(noteId: Int, isLocked: Boolean) {
        dao.setLockedNote(noteId, isLocked)
    }

}
