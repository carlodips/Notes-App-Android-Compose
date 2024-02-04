package dev.carlodips.notes_compose.ui.screens.notes_list

import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.utils.NoteListMode

sealed class NotesListUiEvent {
    data class DeleteNote(val note: Note) : NotesListUiEvent()
    data object UndoDeleteNote: NotesListUiEvent()
    data class DrawerMenuClick(val mode: NoteListMode) : NotesListUiEvent()
}