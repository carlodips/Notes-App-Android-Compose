package dev.carlodips.notes_compose.ui.screens.notes_list

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.ui.screens.add_edit_note.util.AddEditNoteResultEvent
import dev.carlodips.notes_compose.ui.screens.notes_list.util.NotesListResultEvent
import dev.carlodips.notes_compose.ui.screens.notes_list.util.NotesListUiEvent
import dev.carlodips.notes_compose.utils.NavigationItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO:
//  1. Re-implement delete function
//  2. Add Search functionality
//  3. Add different view

@Composable
fun NotesListScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel = hiltViewModel(),
    messageFromAddEdit: String,
    onNavigateToAddEdit: (route: String) -> Unit
) {
    val notesList = viewModel.notesList.collectAsState(initial = emptyList())
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(messageFromAddEdit) {
        if (messageFromAddEdit.isNotEmpty()) {
            snackBarHostState.showSnackbar(
                message = messageFromAddEdit,
                duration = SnackbarDuration.Short
            )
        }
    }

    /*LaunchedEffect(uiState.value.shouldShowSnackbar) {
        if (uiState.value.shouldShowSnackbar) {
            val snackBarResult = snackBarHostState.showSnackbar(
                message = uiState.value.snackbarMessage,
                actionLabel = uiState.value.snackbarActionLabel,
                duration = SnackbarDuration.Short
            )

            when (snackBarResult) {
                SnackbarResult.ActionPerformed -> {
                    viewModel.onUndoDeleteNoteClick()
                }

                SnackbarResult.Dismissed -> {
                    viewModel.dismissSnackbar()
                }
            }
        }
    }*/

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    onNavigateToAddEdit.invoke(NavigationItem.AddEditNote.route)
                }) {
                    Icon(Icons.Filled.Add, null)
                }
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier.padding(
                            horizontal = 16.dp
                        ),
                        text = stringResource(R.string.notes),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (notesList.value.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            ),
                            text = stringResource(R.string.msg_no_notes_added),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(notesList.value) { note ->
                        NoteItem(
                            note = note,
                            onEditClick = {
                                onNavigateToAddEdit.invoke(
                                    NavigationItem.AddEditNote.route + "?noteId=${note.noteId}"
                                )
                            },
                            onDeleteClick = {
                                viewModel.onEvent(NotesListUiEvent.DeleteNote(note))
                                scope.launch {
                                    val snackBarResult = snackBarHostState.showSnackbar(
                                        message = uiState.value.snackbarMessage,
                                        actionLabel = uiState.value.snackbarActionLabel,
                                        duration = SnackbarDuration.Short
                                    )

                                    if (snackBarResult == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(NotesListUiEvent.UndoDeleteNote)
                                    }
                                }


                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NotesListResultEvent.ShowSnackbar -> {
                    if (event.message.isNotEmpty()) {
                        snackBarHostState.showSnackbar(
                            message = uiState.value.snackbarMessage,
                            actionLabel = uiState.value.snackbarActionLabel,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }
}
