package dev.carlodips.notes_compose.ui.screens.add_edit_note.util

sealed class AddEditNoteResultEvent {
    data object NoteSaved : AddEditNoteResultEvent()
    data object NoteDeleted : AddEditNoteResultEvent()
    data class NoteDiscarded(val isEdit: Boolean = false) : AddEditNoteResultEvent()
}