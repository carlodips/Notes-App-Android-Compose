package dev.carlodips.notes_compose.ui.screens.add_edit_note

import dev.carlodips.notes_compose.data.local.entity.Note

data class AddEditNoteUiState(
    val note: Note? = null, // For edit
    val title: String,
    val body: String,
    val isDoneSaving: Boolean
) {
    companion object {
        val DEFAULT = AddEditNoteUiState(
            note = null,
            title = "",
            body = "",
            isDoneSaving = false
        )
    }
}