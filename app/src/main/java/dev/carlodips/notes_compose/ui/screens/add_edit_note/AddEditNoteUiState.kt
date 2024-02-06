package dev.carlodips.notes_compose.ui.screens.add_edit_note

import dev.carlodips.notes_compose.utils.ScreenMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AddEditNoteUiState(
    val title: String,
    val body: String,
    val lastEdited: String,
    //val isDoneSaving: Boolean,
    val screenMode: ScreenMode,
    //val hasDiscardNote: Boolean,
    val shouldAutoFocusBody: Boolean,
    val isArchived: Boolean,
    val isLocked: Boolean
    //val isDoneDeleting: Boolean
) {
    companion object {
        val DEFAULT = AddEditNoteUiState(
            title = "",
            body = "",
            lastEdited = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy, HH:mm:ss")),
            //isDoneSaving = false,
            screenMode = ScreenMode.ADD,
//            hasDiscardNote = false,
            shouldAutoFocusBody = false,
            isArchived = false,
            isLocked = false
//            isDoneDeleting = false
        )
    }
}
