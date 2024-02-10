package dev.carlodips.notes_compose.ui.screens.notes_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.ui.screens.folders.component.FolderLazyRow
import dev.carlodips.notes_compose.ui.screens.navigation_drawer.NavigationDrawer
import dev.carlodips.notes_compose.utils.NoteListMode
import dev.carlodips.notes_compose.utils.ScreenRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO:
//  1. Re-implement delete function
//  3. Add different view
//  4. Add folder horizontal list

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesListScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel = hiltViewModel(),
    messageFromAddEdit: String,
    onNavigateToAddEdit: (route: String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFolderList: () -> Unit
) {
    val notesList = viewModel.notesList.collectAsState(initial = null)
    val folderList = viewModel.foldersList.collectAsState(initial = null)

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val navDrawerUiState = viewModel.navDrawerUiState.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    LaunchedEffect(messageFromAddEdit) {
        if (messageFromAddEdit.isNotEmpty()) {
            snackBarHostState.showSnackbar(
                message = messageFromAddEdit,
                duration = SnackbarDuration.Short
            )
        }
    }

    NavigationDrawer(
        uiState = navDrawerUiState.value,
        drawerState = drawerState,
        onNavigate = {
            viewModel.onEvent(NotesListUiEvent.DrawerMenuClick(it))
        }
    ) {
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
                        onNavigateToAddEdit.invoke(ScreenRoute.AddEditNote.route)
                    }) {
                        Icon(Icons.Filled.Add, null)
                    }
                },
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            val appBarTitle = when (navDrawerUiState.value.selectedMode) {
                                NoteListMode.ALL -> {
                                    stringResource(id = R.string.notes)
                                }

                                NoteListMode.LOCKED -> {
                                    stringResource(id = R.string.menu_locked_notes)
                                }

                                else -> {
                                    stringResource(id = R.string.menu_archived_notes)
                                }
                            }

                            Text(
                                appBarTitle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                onNavigateToSearch.invoke()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null
                                )
                            }

                            IconButton(onClick = {
                                // TODO: Show menu
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = null
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CompositionLocalProvider(
                                LocalOverscrollConfiguration provides null
                            ) {
                                FolderLazyRow(
                                    list = folderList.value,
                                    onItemClick = {
                                        viewModel.onEvent(NotesListUiEvent.FolderClick(it.folderId))
                                    },
                                    selectedFolderId = uiState.value.selectedFolderId
                                )
                            }

                            ElevatedButton(
                                onClick = {
                                    onNavigateToFolderList.invoke()
                                }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_folder),
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (notesList.value == null) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(32.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            }
                        }
                    } else if (notesList.value!!.isEmpty()) {
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
                        items(notesList.value!!) { note ->
                            NoteItem(
                                note = note,
                                onItemClick = {
                                    onNavigateToAddEdit.invoke(
                                        ScreenRoute.AddEditNote.route + "?noteId=${note.noteId}"
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
