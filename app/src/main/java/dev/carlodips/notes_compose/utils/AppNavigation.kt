package dev.carlodips.notes_compose.utils

sealed class NavigationItem(val route: String) {
    data object NotesList : NavigationItem("notes_list") {
        const val MESSAGE = "message"
    }
    data object AddEditNote : NavigationItem("add_edit") {
        const val NOTE_ID = "noteId"
    }
    data object Search : NavigationItem("search")
}
