package dev.carlodips.notes_compose.ui.screens.add_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import dev.carlodips.notes_compose.utils.NavigationItem
import dev.carlodips.notes_compose.utils.ScreenMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
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

    private var dateAdded: LocalDateTime? = null // For edit
    private var oldNote: Note? = null // For revert

    init {
        val noteId = savedStateHandle.get<Int>(NavigationItem.AddEditNote.NOTE_ID) ?: -1

        if (noteId != -1) {
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note ->
                    oldNote = note
                    dateAdded = note.dateAdded
                    _uiState.update {
                        it.copy(
                            noteId = note.noteId,
                            title = note.noteTitle,
                            body = note.noteBody,
                            lastEdited = note.formattedDateUpdated,
                            screenMode = ScreenMode.VIEW
                        )
                    }
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    screenMode = ScreenMode.ADD
                )
            }
            setShouldFocus(shouldFocus = true)
        }
    }

    fun setShouldFocus(shouldFocus: Boolean) {
        _uiState.update {
            it.copy(
                shouldFocus = shouldFocus
            )
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

    fun onUndoChanges() {
        if (oldNote == null) return

        _uiState.update {
            it.copy(
                noteId = oldNote!!.noteId,
                title = oldNote!!.noteTitle,
                body = oldNote!!.noteBody,
                lastEdited = oldNote!!.formattedDateUpdated,
                screenMode = ScreenMode.VIEW
            )
        }
    }

    fun setScreenMode(screenMode: ScreenMode) {
        _uiState.update {
            it.copy(
                screenMode = screenMode
            )
        }
    }

    fun onDoneSaving() {
        _uiState.update {
            it.copy(isDoneSaving = false)
        }
    }

    fun onSaveNote() {
        if (uiState.value.title.isBlank() && uiState.value.body.isBlank()) {
            _uiState.update {
                it.copy(
                    hasMessage = true,
                    message = R.string.msg_note_discarded,
                    isDoneSaving = true
                )
            }
            return
        }

        viewModelScope.launch {
            repository.insertNote(
                Note(
                    noteId = uiState.value.noteId,
                    noteTitle = uiState.value.title,
                    noteBody = uiState.value.body,
                    dateAdded = dateAdded ?: LocalDateTime.now(),
                    dateUpdated = LocalDateTime.now()
                )
            )

            _uiState.update {
                it.copy(isDoneSaving = true)
            }
        }
    }
}
