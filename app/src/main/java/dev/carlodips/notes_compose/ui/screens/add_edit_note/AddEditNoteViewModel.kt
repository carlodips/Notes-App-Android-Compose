package dev.carlodips.notes_compose.ui.screens.add_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.data.local.entity.Note
import dev.carlodips.notes_compose.data.local.repository.NoteRepository
import dev.carlodips.notes_compose.utils.NavigationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AddEditNoteUiState.DEFAULT)
    val uiState: StateFlow<AddEditNoteUiState>
        get() = _uiState.asStateFlow()

    init {
        val noteId = savedStateHandle.get<Int>(NavigationItem.AddEditNote.NOTE_ID) ?: -1

        if (noteId != -1) {
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note ->
                    _uiState.update {
                        it.copy(
                            noteId = note.noteId,
                            title = note.noteTitle,
                            body = note.noteBody,
                            isEdit = true
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun onBodyChange(body: String) {
        _uiState.update {
            it.copy(body = body)
        }
    }

    /*fun onDoneSaving() {
        _uiState.update {
            it.copy(isDoneSaving = false)
        }
    }*/

    fun onSaveNoteClick() {
        if (uiState.value.title.isBlank() || uiState.value.body.isBlank()) {
            _uiState.update {
                it.copy(
                    isError = true,
                    errorMessage = R.string.msg_fields_empty
                )
            }
            return
        }

        viewModelScope.launch {
            repository.insertNote(
                Note(
                    noteId = uiState.value.noteId,
                    noteTitle = uiState.value.title,
                    noteBody = uiState.value.body
                )
            )

            _uiState.update {
                it.copy(isDoneSaving = true)
            }
        }
    }

    fun dismissSnackBar() {
        _uiState.update {
            it.copy(
                isError = false,
                errorMessage = -1
            )
        }
    }
}
