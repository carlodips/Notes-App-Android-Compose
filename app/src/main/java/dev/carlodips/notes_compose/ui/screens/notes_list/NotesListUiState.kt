package dev.carlodips.notes_compose.ui.screens.notes_list

data class NotesListUiState(
    //val shouldShowSnackbar: Boolean,
    val snackbarMessage: String,
    val snackbarActionLabel: String,
    val selectedFolderId: Int?
) {
    companion object {
        val DEFAULT = NotesListUiState(
            //shouldShowSnackbar = false,
            snackbarMessage = "",
            snackbarActionLabel = "",
            selectedFolderId = 0
        )
    }
}