package dev.carlodips.notes_compose.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.ui.component.BaseCard
import dev.carlodips.notes_compose.ui.component.CustomSearchBar
import dev.carlodips.notes_compose.ui.screens.notes_list.NoteItem
import dev.carlodips.notes_compose.ui.screens.search.util.SearchUiEvent
import dev.carlodips.notes_compose.utils.NavigationItem

// TODO:
//  1. Highlight yung part ng text na matched dun sa query
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onPopBackStack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToViewNote: (route: String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val notesList by viewModel.notesList.collectAsState(initial = emptyList())

    val focusRequester = remember { FocusRequester() }

    // Auto focus onStart only during Search
    LaunchedEffect(uiState.value.shouldAutoFocusBody) {
        if (uiState.value.shouldAutoFocusBody) {
            focusRequester.requestFocus()
            viewModel.onUiEvent(SearchUiEvent.AutoFocusedSearch)
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                BaseCard(isEnabled = true) {
                    CustomSearchBar(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .focusRequester(focusRequester),
                        query = uiState.value.query,
                        onQueryChange = {
                            viewModel.onUiEvent(SearchUiEvent.EnteredQuery(it))
                        },
                        leadingIcon = {
                            IconButton(onClick = { onPopBackStack.invoke() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                // TODO: Show menu
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = null
                                )
                            }
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.placeholder_search),
                                color = Color.Gray
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    items(notesList) { note ->
                        NoteItem(
                            note = note,
                            onItemClick = {
                                onNavigateToViewNote.invoke(
                                    NavigationItem.AddEditNote.route + "?noteId=${note.noteId}"
                                )
                            },
                            onDeleteClick = {}
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
