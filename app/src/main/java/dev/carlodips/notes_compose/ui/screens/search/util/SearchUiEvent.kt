package dev.carlodips.notes_compose.ui.screens.search.util

sealed class SearchUiEvent {
    data class EnteredQuery(val query: String) : SearchUiEvent()
    data object ToggleSearch : SearchUiEvent()
    data object AutoFocusedSearch : SearchUiEvent()
}