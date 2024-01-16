package dev.carlodips.notes_compose.data.local.repository

import dev.carlodips.notes_compose.data.local.entity.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepository : NoteRepository {

    private val notesList = mutableListOf<Note>()

    override suspend fun insertNote(note: Note) {
        notesList.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notesList.remove(note)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return notesList.find {
            it.noteId == id
        }
    }

    override fun getNotes(): Flow<List<Note>> {
        return flow { emit(notesList.toList()) }
    }
}