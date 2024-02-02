package dev.carlodips.notes_compose.ui.screens.add_edit_note.util

sealed class AddEditNoteUiEvent {
    data class EnteredTitle(val title: String) : AddEditNoteUiEvent()

    data class EnteredBody(val body: String) : AddEditNoteUiEvent()

    data object DeleteNote : AddEditNoteUiEvent()

    data object UndoChanges : AddEditNoteUiEvent()

    data object SaveNote : AddEditNoteUiEvent()

    data object AutoFocusedBody : AddEditNoteUiEvent()
}