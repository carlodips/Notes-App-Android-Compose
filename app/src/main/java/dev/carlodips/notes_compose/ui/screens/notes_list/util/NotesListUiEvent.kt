package dev.carlodips.notes_compose.ui.screens.notes_list.util

import dev.carlodips.notes_compose.domain.model.Note

sealed class NotesListUiEvent {
    data class DeleteNote(val note: Note) : NotesListUiEvent()
    data object UndoDeleteNote: NotesListUiEvent()
}