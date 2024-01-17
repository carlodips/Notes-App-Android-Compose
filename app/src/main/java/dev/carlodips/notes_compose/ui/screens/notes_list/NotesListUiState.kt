package dev.carlodips.notes_compose.ui.screens.notes_list

data class NotesListUiState(
    val shouldShowSnackBar: Boolean,
    val snackbarMessage: String,
    val snackbarActionLabel: String
) {
    companion object {
        val DEFAULT = NotesListUiState(
            shouldShowSnackBar = false,
            snackbarMessage = "",
            snackbarActionLabel = ""
        )
    }
}