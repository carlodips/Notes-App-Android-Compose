package dev.carlodips.notes_compose.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationMenu(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String
)