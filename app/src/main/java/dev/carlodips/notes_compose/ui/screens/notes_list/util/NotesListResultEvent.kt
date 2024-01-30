package dev.carlodips.notes_compose.ui.screens.notes_list.util

import dev.carlodips.notes_compose.ui.screens.add_edit_note.util.AddEditNoteResultEvent

sealed class NotesListResultEvent {
    data class ShowSnackbar(val message: String) : NotesListResultEvent()
}