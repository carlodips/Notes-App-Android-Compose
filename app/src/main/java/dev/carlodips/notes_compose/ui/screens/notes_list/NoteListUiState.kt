package dev.carlodips.notes_compose.ui.screens.notes_list

data class NoteListUiState(
    val shouldShowSnackBar: Boolean,
    val snackBarMessage: String,
    val snackBarActionLabel: String
) {
    companion object {
        val DEFAULT = NoteListUiState(
            shouldShowSnackBar = false,
            snackBarMessage = "",
            snackBarActionLabel = ""
        )
    }
}