package dev.carlodips.notes_compose.ui.screens.search

data class SearchUiState(
    val query: String,
    val isSearching: Boolean,
    val shouldAutoFocusBody: Boolean,
) {
    companion object {
        val DEFAULT = SearchUiState(
            query = "",
            isSearching = false,
            shouldAutoFocusBody = false
        )
    }
}