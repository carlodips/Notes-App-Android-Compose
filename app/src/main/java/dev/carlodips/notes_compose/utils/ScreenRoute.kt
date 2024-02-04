package dev.carlodips.notes_compose.utils

sealed class ScreenRoute(val route: String) {
    data object NotesList : ScreenRoute("notes_list") {
        const val MESSAGE = "message"
    }
    data object AddEditNote : ScreenRoute("add_edit") {
        const val NOTE_ID = "noteId"
    }
    data object Search : ScreenRoute("search")
}
