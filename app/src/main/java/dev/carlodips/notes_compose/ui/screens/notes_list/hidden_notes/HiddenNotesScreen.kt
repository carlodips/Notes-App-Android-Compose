package dev.carlodips.notes_compose.ui.screens.notes_list.hidden_notes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.carlodips.notes_compose.ui.screens.navigation_drawer.NavigationDrawer

@Composable
fun HiddenNotesScreen(
    viewModel: HiddenNotesViewModel = hiltViewModel(),
    onNavigateNavDrawer: (String) -> Unit
) {
    val navDrawerUiState = viewModel.navDrawerUiState.collectAsStateWithLifecycle()

    NavigationDrawer(
        uiState = navDrawerUiState.value,
        onNavigate = onNavigateNavDrawer
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // TODO
            Text(text = "Hidden Notes screen")
        }
    }
}