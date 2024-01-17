package dev.carlodips.notes_compose.ui.screens.add_edit_note

import androidx.annotation.StringRes

data class AddEditNoteUiState(
    val noteId: Int, // For edit
    val title: String,
    val body: String,
    val isDoneSaving: Boolean,
    val isEdit: Boolean,
    val isError: Boolean,
    @StringRes val message: Int
) {
    companion object {
        val DEFAULT = AddEditNoteUiState(
            noteId = 0,
            title = "",
            body = "",
            isDoneSaving = false,
            isEdit = false,
            isError = false,
            message = -1
        )
    }
}
