package dev.carlodips.notes_compose.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import dev.carlodips.notes_compose.utils.NoteListMode

data class NavigationMenu(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val noteListMode: NoteListMode
)