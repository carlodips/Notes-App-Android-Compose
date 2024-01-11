package dev.carlodips.notes_compose.ui.screens.notes_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.data.local.entity.Note
import dev.carlodips.notes_compose.data.local.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val notesList = repository.getNotes()

    /*val uiState: StateFlow<NotesListUiState>
        get() = _uiState.asStateFlow()*/

    /*fun onUIEvent(event: NotesListEvent) {
        when (event) {
            is NotesListEvent.OnNoteClick -> {

            }

            is NotesListEvent.OnAddNoteClick -> {
                _uiState.update {
                    UIState.Navigate(Screens.NotesList.route)
                }
            }

            is NotesListEvent.OnDeleteNoteClick -> {

            }

            is NotesListEvent.OnUndoDeleteClick -> {

            }

            is NotesListEvent.OnDoneChange -> {

            }
        }
    }*/

    // Trigger event
    /*private fun sendUiEvent(event: UIState) {
        *//*viewModelScope.launch {
            _uiEvent.send(event)
        }*//*
    }*/
}
