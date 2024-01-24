package dev.carlodips.notes_compose.ui.screens.add_edit_note

import androidx.annotation.StringRes
import dev.carlodips.notes_compose.utils.ScreenMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AddEditNoteUiState(
    val noteId: Int, // For edit
    val title: String,
    val body: String,
    val lastEdited: String,
    val isDoneSaving: Boolean,
    val screenMode: ScreenMode,
    val hasMessage: Boolean,
    @StringRes val message: Int,
    val shouldFocus: Boolean
) {
    companion object {
        val DEFAULT = AddEditNoteUiState(
            noteId = 0,
            title = "",
            body = "",
            lastEdited = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy, HH:mm:ss")),
            isDoneSaving = false,
            screenMode = ScreenMode.ADD,
            hasMessage = false,
            message = -1,
            shouldFocus = false
        )
    }
}

