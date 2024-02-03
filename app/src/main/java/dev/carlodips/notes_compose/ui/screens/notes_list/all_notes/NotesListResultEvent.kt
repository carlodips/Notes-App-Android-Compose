package dev.carlodips.notes_compose.ui.screens.notes_list.all_notes

sealed class NotesListResultEvent {
    data class ShowSnackbar(val message: String) : NotesListResultEvent()
}