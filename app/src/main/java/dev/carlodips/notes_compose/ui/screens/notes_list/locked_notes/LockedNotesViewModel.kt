package dev.carlodips.notes_compose.ui.screens.notes_list.locked_notes

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import dev.carlodips.notes_compose.ui.screens.navigation_drawer.NavigationDrawerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LockedNotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    // TODO: Not sure kung hardcoded na index dapat to
    // TODO: Pwede pala gawin is gawa ako BaseViewModel tapos andun yung navDrawerUiState para reusable
    private val _navDrawerUiState = MutableStateFlow(NavigationDrawerUiState(selectedItemIndex = 1))
    val navDrawerUiState: StateFlow<NavigationDrawerUiState>
        get() = _navDrawerUiState.asStateFlow()
}