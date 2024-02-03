package dev.carlodips.notes_compose.ui.screens.navigation_drawer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.domain.model.NavigationMenu
import dev.carlodips.notes_compose.utils.NavigationItem
import kotlinx.coroutines.launch

// TODO: Add Delete menu
@Composable
fun NavigationDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    uiState: NavigationDrawerUiState,
    onNavigate: (String) -> Unit,
    content: @Composable () -> Unit
) {
    val items = listOf(
        NavigationMenu(
            title = stringResource(id = R.string.notes),
            selectedIcon = Icons.Filled.List,
            unselectedIcon = Icons.Outlined.List,
            route = NavigationItem.NotesList.route
        ),
        NavigationMenu(
            title = stringResource(R.string.menu_locked_notes),
            selectedIcon = Icons.Filled.Lock,
            unselectedIcon = Icons.Outlined.Lock,
            badgeCount = 45,
            route = NavigationItem.LockedNotes.route
        ),
        NavigationMenu(
            title = stringResource(R.string.menu_hidden_notes),
            selectedIcon = Icons.Filled.Face,
            unselectedIcon = Icons.Outlined.Face,
            route = NavigationItem.HiddenNotes.route
        )
    )

    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = index == uiState.selectedItemIndex,
                        onClick = {
                            if (index != uiState.selectedItemIndex) {
                                onNavigate.invoke(item.route)
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == uiState.selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        badge = {
                            item.badgeCount?.let {
                                Text(text = item.badgeCount.toString())
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState,
        content = content
    )
}