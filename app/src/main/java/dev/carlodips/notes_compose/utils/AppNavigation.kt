package dev.carlodips.notes_compose.utils

enum class Screen {
    NotesList,
    AddEditNote,
}
sealed class NavigationItem(val route: String) {
    object NotesList : NavigationItem(Screen.NotesList.name)
    object AddEditNote : NavigationItem(Screen.AddEditNote.name) {
        const val NOTE_ID = "noteId"
    }
}
