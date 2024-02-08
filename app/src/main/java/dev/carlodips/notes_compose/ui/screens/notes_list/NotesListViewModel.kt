package dev.carlodips.notes_compose.ui.screens.notes_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.domain.model.Folder
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.domain.repository.FolderRepository
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
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesListUiState.DEFAULT)
    val uiState: StateFlow<NotesListUiState>
        get() = _uiState.asStateFlow()

    private val _navDrawerUiState =
        MutableStateFlow(
            NavigationDrawerUiState(
                selectedMode = NoteListMode.ALL,
                allNotesCount = noteRepository.getAllNotesCount(),
                lockedNotesCount = noteRepository.getLockedNotesCount(),
                archivedNotesCount = noteRepository.getArchiveNotesCount()
            )
        )

    val navDrawerUiState: StateFlow<NavigationDrawerUiState>
        get() = _navDrawerUiState.asStateFlow()

    private val _notesList = noteRepository.getNotes()
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

    private val _foldersList = folderRepository.getFolders()
    val foldersList: StateFlow<List<Folder>> = _foldersList.transform { folderList ->
        // Show "All" and folder button only
        val newList = arrayListOf<Folder>()
        newList.add(Folder.ALL_NOTES)

        if (folderList.isNotEmpty()) {
            newList.add(Folder.UNCATEGORIZED)
            newList.addAll(folderList)
        }

        emit(newList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _eventFlow = MutableSharedFlow<NotesListResultEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var deletedNote: Note? = null

    fun onEvent(event: NotesListUiEvent) {
        when (event) {
            is NotesListUiEvent.DeleteNote -> onDeleteNoteClick(event.note)
            is NotesListUiEvent.UndoDeleteNote -> onUndoDeleteNoteClick()
            is NotesListUiEvent.DrawerMenuClick -> onDrawerMenuClick(event.mode)
            is NotesListUiEvent.FolderClick -> onFolderClick(event.selectedFolderId)
        }
    }

    private fun onDeleteNoteClick(note: Note) {
        viewModelScope.launch {
            deletedNote = note
            noteRepository.deleteNote(note)
            /*showSnackbar(
                snackbarMessage = app.getString(R.string.msg_note_deleted),
                snackbarActionLabel = app.getString(R.string.undo)
            )*/
        }
    }

    fun onUndoDeleteNoteClick() {
        deletedNote?.let {
            viewModelScope.launch {
                noteRepository.insertNote(it)
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

    private fun onFolderClick(folderId: Int?) {
        Log.v("onFolderClick", "folder clicked  $folderId")
        _uiState.update {
            it.copy(
                selectedFolderId = folderId
            )
        }
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
