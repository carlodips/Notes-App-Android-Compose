package dev.carlodips.notes_compose.ui.screens.add_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import dev.carlodips.notes_compose.ui.screens.add_edit_note.util.AddEditNoteResultEvent
import dev.carlodips.notes_compose.ui.screens.add_edit_note.util.AddEditNoteUiEvent
import dev.carlodips.notes_compose.utils.ScreenMode
import dev.carlodips.notes_compose.utils.ScreenRoute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _uiState = MutableStateFlow(AddEditNoteUiState.DEFAULT)
    val uiState: StateFlow<AddEditNoteUiState>
        get() = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AddEditNoteResultEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null
    private var dateAdded: LocalDateTime? = null // For edit
    private var oldNote: Note? = null // For revert

    init {
        val noteId = savedStateHandle.get<Int>(ScreenRoute.AddEditNote.NOTE_ID) ?: -1

        if (noteId != -1) {
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note ->
                    oldNote = note
                    currentNoteId = note.noteId
                    dateAdded = note.dateAdded
                    _uiState.update {
                        it.copy(
                            title = note.noteTitle,
                            body = note.noteBody,
                            lastEdited = note.formattedDateUpdated,
                            screenMode = ScreenMode.VIEW,
                            isHidden = note.isNoteHidden,
                            isLocked = note.isNoteLocked
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
            setShouldAutoFocusBody(shouldFocus = true)
        }
    }

    fun onUiEvent(event: AddEditNoteUiEvent) {
        when (event) {
            is AddEditNoteUiEvent.EnteredTitle -> {
                _uiState.update {
                    it.copy(title = event.title)
                }
            }

            is AddEditNoteUiEvent.EnteredBody -> {
                _uiState.update {
                    it.copy(body = event.body)
                }
            }

            is AddEditNoteUiEvent.UndoChanges -> {
                onUndoChanges()
            }

            is AddEditNoteUiEvent.DeleteNote -> {
                onDeleteNote()
            }

            is AddEditNoteUiEvent.HideNote -> {
                onHideNote(shouldHide = event.shouldHide)
            }

            is AddEditNoteUiEvent.LockNote -> {
                onLockedNote(shouldLock = event.shouldLock)
            }

            is AddEditNoteUiEvent.SaveNote -> {
                onSaveNote()
            }

            is AddEditNoteUiEvent.AutoFocusedBody -> {
                setShouldAutoFocusBody(false)
            }
        }
    }


    // TODO: Not sure if this is the right way
    private fun setShouldAutoFocusBody(shouldFocus: Boolean) {
        _uiState.update {
            it.copy(shouldAutoFocusBody = shouldFocus)
        }
    }

    /*fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun onBodyChange(body: String) {
        _uiState.update {
            it.copy(body = body)
        }
    }*/

    private fun onUndoChanges() {
        if (oldNote == null) return

        _uiState.update {
            it.copy(
                title = oldNote!!.noteTitle,
                body = oldNote!!.noteBody,
                lastEdited = oldNote!!.formattedDateUpdated,
                screenMode = ScreenMode.VIEW
            )
        }
    }

    private fun onDeleteNote() {
        viewModelScope.launch {
            oldNote?.let { repository.deleteNote(it) }
            _eventFlow.emit(AddEditNoteResultEvent.NoteDeleted)
        }
    }

    private fun onHideNote(shouldHide: Boolean) {
        viewModelScope.launch {
            currentNoteId?.let {
                repository.setHiddenNote(
                    noteId = it,
                    isHidden = shouldHide
                )
            }
            _eventFlow.emit(AddEditNoteResultEvent.NoteHidden)
        }
    }

    private fun onLockedNote(shouldLock: Boolean) {
        viewModelScope.launch {
            currentNoteId?.let {
                repository.setLockedNote(
                    noteId = it,
                    isLocked = shouldLock
                )
            }
            _eventFlow.emit(AddEditNoteResultEvent.NoteLocked)
        }
    }

    fun setScreenMode(screenMode: ScreenMode) {
        _uiState.update {
            it.copy(screenMode = screenMode)
        }
    }

    /*fun onDoneSaving() {
        _uiState.update {
            it.copy(isDoneSaving = false)
        }
    }*/

    private fun onSaveNote() {
        if (uiState.value.title.isBlank() && uiState.value.body.isBlank()) {
            if (uiState.value.screenMode == ScreenMode.ADD) {
                viewModelScope.launch {
                    _eventFlow.emit(AddEditNoteResultEvent.NoteDiscarded(isEdit = false))
                }
            } else {
                viewModelScope.launch {
                    oldNote?.let { repository.deleteNote(it) }
                    _eventFlow.emit(AddEditNoteResultEvent.NoteDiscarded(isEdit = true))
                }
            }
            return
        }

        val noteToBeSaved = Note(
            noteId = currentNoteId,
            noteTitle = uiState.value.title,
            noteBody = uiState.value.body,
            dateAdded = dateAdded ?: LocalDateTime.now(),
            dateUpdated = LocalDateTime.now(),
            isNoteHidden = uiState.value.isHidden,
            isNoteLocked = uiState.value.isLocked
        )

        viewModelScope.launch {
            currentNoteId = repository.insertNote(noteToBeSaved).toInt()

            // Overwrite current values being cached in this viewmodel after inserting/updating note
            oldNote = noteToBeSaved.copy(
                noteId = currentNoteId
            )

            dateAdded = noteToBeSaved.dateAdded

            _uiState.update {
                it.copy(
                    lastEdited = noteToBeSaved.formattedDateUpdated
                )
            }

            setScreenMode(ScreenMode.VIEW)
            _eventFlow.emit(AddEditNoteResultEvent.NoteSaved)
        }
    }
}
