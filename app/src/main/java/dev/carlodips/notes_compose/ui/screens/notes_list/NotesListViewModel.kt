package dev.carlodips.notes_compose.ui.screens.notes_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.data.local.entity.Note
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val app: Application,
    private val repository: NoteRepository
) : AndroidViewModel(app) {

    val notesList = repository.getNotes()

    private val _uiState =
        MutableStateFlow(NotesListUiState.DEFAULT)
    val uiState: StateFlow<NotesListUiState>
        get() = _uiState.asStateFlow()

    private var deletedNote: Note? = null

    fun onDeleteNoteClick(note: Note) {
        viewModelScope.launch {
            deletedNote = note
            repository.deleteNote(note)
            showSnackbar(
                snackbarMessage = app.getString(R.string.note_deleted),
                snackbarActionLabel = app.getString(R.string.undo)
            )
        }
    }

    fun onUndoDeleteNoteClick() {
        deletedNote?.let {
            viewModelScope.launch {
                repository.insertNote(it)
            }
        }
        dismissSnackbar()
    }

    fun showSnackbar(snackbarMessage: String, snackbarActionLabel: String = "") {
        _uiState.update {
            it.copy(
                shouldShowSnackBar = true,
                snackbarMessage = snackbarMessage,
                snackbarActionLabel = snackbarActionLabel
            )
        }
    }

    fun dismissSnackbar() {
        _uiState.update {
            it.copy(
                shouldShowSnackBar = false,
                snackbarMessage = "",
                snackbarActionLabel = ""
            )
        }
    }
}
