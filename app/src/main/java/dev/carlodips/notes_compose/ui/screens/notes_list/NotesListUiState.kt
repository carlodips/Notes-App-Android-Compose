package dev.carlodips.notes_compose.ui.screens.notes_list

data class NotesListUiState(
    val shouldShowSnackbar: Boolean,
    val snackbarMessage: String,
    val snackbarActionLabel: String
) {
    companion object {
        val DEFAULT = NotesListUiState(
            shouldShowSnackbar = false,
            snackbarMessage = "",
            snackbarActionLabel = ""
        )
    }
}