package dev.carlodips.notes_compose.ui.screens.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import dev.carlodips.notes_compose.ui.screens.navigation_drawer.NavigationDrawerUiState
import dev.carlodips.notes_compose.utils.NoteListMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesListUiState.DEFAULT)
    val uiState: StateFlow<NotesListUiState>
        get() = _uiState.asStateFlow()

    private val _navDrawerUiState =
        MutableStateFlow(NavigationDrawerUiState(
            selectedMode = NoteListMode.ALL,
            allNotesCount = repository.getAllNotesCount(),
            lockedNotesCount = repository.getLockedNotesCount(),
            archivedNotesCount = repository.getArchiveNotesCount()
        ))

    val navDrawerUiState: StateFlow<NavigationDrawerUiState>
        get() = _navDrawerUiState.asStateFlow()

    private val _notesList = repository.getNotes()
    val notesList: StateFlow<List<Note>> = combine(
        navDrawerUiState, _notesList
    ) { navDrawerUiState, notes ->
        val mode = navDrawerUiState.selectedMode
        if (mode == NoteListMode.ALL) {
            notes.filter { !it.isNoteArchived }
        } else {
            if (mode == NoteListMode.ARCHIVED) {
                notes.filter { it.isNoteArchived }
            } else {
                notes.filter { it.isNoteLocked }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        //setupDrawerStateBadgeCount()
    }

    private val _eventFlow = MutableSharedFlow<NotesListResultEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var deletedNote: Note? = null

    fun onEvent(event: NotesListUiEvent) {
        when (event) {
            is NotesListUiEvent.DeleteNote -> onDeleteNoteClick(event.note)
            is NotesListUiEvent.UndoDeleteNote -> onUndoDeleteNoteClick()
            is NotesListUiEvent.DrawerMenuClick -> onDrawerMenuClick(event.mode)
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

    private fun onDrawerMenuClick(mode: NoteListMode) {
        _navDrawerUiState.update {
            it.copy(
                selectedMode = mode
            )
        }
    }

    /*private fun setupDrawerStateBadgeCount() {
        val lockedNotesCount = repository.getLockedNotesCount()


        _navDrawerUiState.update {
            it.copy(
                allNotesCount = 0,
                lockedNotesCount = lockedNotesCount.
            )
        }
    }*/

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
