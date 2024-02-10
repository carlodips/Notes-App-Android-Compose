package dev.carlodips.notes_compose.ui.screens.folders

sealed class FolderListUiEvent {
    //data class FolderItemClick(val mode: NoteListMode) : FolderListUiEvent

    data object AddFolder: FolderListUiEvent()
}