package dev.carlodips.notes_compose.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.domain.repository.NoteRepository
import dev.carlodips.notes_compose.ui.screens.search.util.SearchUiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Reference: https://medium.com/@desilio/searchbar-with-jetpack-compose-and-material-design-3-1f735f383c1f
// https://medium.com/@yusufyildiz0441/how-to-use-the-combine-function-to-combine-states-in-a-sample-login-app-7193dea89da0
@HiltViewModel
class SearchViewModel @Inject constructor(
    repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState.DEFAULT)
    val uiState: StateFlow<SearchUiState>
        get() = _uiState.asStateFlow()

    private val _notesList = repository.getNotes()
    val notesList: StateFlow<List<Note>> =
        combine(uiState, _notesList) { searchUiState, notes -> //combine query with _noteList
            val query = searchUiState.query
            if (query.isBlank()) { //return the whole list of notes if not is typed
                emptyList()
            } else {
                notes.filter { note -> // filter list based on the query
                    note.doesMatchSearchQuery(query)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        setShouldAutoFocusBody(shouldFocus = true)
    }

    fun onUiEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.EnteredQuery -> {
                _uiState.update {
                    it.copy(query = event.query)
                }
            }

            SearchUiEvent.ToggleSearch -> {
                val isSearching = !uiState.value.isSearching

                _uiState.update {
                    it.copy(isSearching = isSearching)
                }
            }

            is SearchUiEvent.AutoFocusedSearch -> {
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
}
