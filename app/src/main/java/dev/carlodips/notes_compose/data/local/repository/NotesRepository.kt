package dev.carlodips.notes_compose.data.local.repository

import dev.carlodips.notes_compose.data.local.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun getNoteById(id: Int): Note?

    fun getNotes(): Flow<List<Note>>
}