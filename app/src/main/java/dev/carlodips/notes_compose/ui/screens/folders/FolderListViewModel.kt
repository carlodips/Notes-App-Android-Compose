package dev.carlodips.notes_compose.ui.screens.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.domain.model.Folder
import dev.carlodips.notes_compose.domain.repository.FolderRepository
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class FolderListViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _foldersList = folderRepository.getFolders()
    val foldersList: StateFlow<List<Folder>> = _foldersList
        .transform { folderList ->
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
}
