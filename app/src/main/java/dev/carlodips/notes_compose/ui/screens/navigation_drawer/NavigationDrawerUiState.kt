package dev.carlodips.notes_compose.ui.screens.navigation_drawer

import dev.carlodips.notes_compose.utils.NoteListMode
import kotlinx.coroutines.flow.Flow

data class NavigationDrawerUiState(
    val selectedMode: NoteListMode,
    val allNotesCount: Flow<Int>,
    val lockedNotesCount: Flow<Int>,
    val archivedNotesCount: Flow<Int>
)