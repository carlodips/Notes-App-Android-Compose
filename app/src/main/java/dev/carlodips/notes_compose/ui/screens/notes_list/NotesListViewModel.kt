package dev.carlodips.notes_compose.ui.screens.notes_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import dev.carlodips.notes_compose.ui.screens.notes_list.util.NotesListResultEvent
import dev.carlodips.notes_compose.ui.screens.notes_list.util.NotesListUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val app: Application,
    private val repository: NoteRepository
) : AndroidViewModel(app) {

    val notesList = repository.getNotes()

    private val _uiState = MutableStateFlow(NotesListUiState.DEFAULT)
    val uiState: StateFlow<NotesListUiState>
        get() = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<NotesListResultEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var deletedNote: Note? = null

    fun onEvent(event: NotesListUiEvent) {
        when (event) {
            is NotesListUiEvent.DeleteNote -> onDeleteNoteClick(event.note)
            is NotesListUiEvent.UndoDeleteNote -> onUndoDeleteNoteClick()
        }
    }

    private fun onDeleteNoteClick(note: Note) {
        viewModelScope.launch {
            deletedNote = note
            repository.deleteNote(note)
            /*showSnackbar(
                snackbarMessage = app.getString(R.string.msg_note_deleted),
                snackbarActionLabel = app.getString(R.string.undo)
            )*/
        }
    }

    fun onUndoDeleteNoteClick() {
        deletedNote?.let {
            viewModelScope.launch {
                repository.insertNote(it)
            }
        }
        //dismissSnackbar()
    }

    /*fun showSnackbar(snackbarMessage: String, snackbarActionLabel: String = "") {
        _uiState.update {
            it.copy(
                shouldShowSnackbar = true,
                snackbarMessage = snackbarMessage,
                snackbarActionLabel = snackbarActionLabel
            )
        }
    }

    fun dismissSnackbar() {
        _uiState.update {
            it.copy(
                shouldShowSnackbar = false,
                snackbarMessage = "",
                snackbarActionLabel = ""
            )
        }
    }*/
}
