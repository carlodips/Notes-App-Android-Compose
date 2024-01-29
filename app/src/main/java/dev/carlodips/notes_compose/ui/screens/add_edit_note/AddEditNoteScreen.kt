package dev.carlodips.notes_compose.ui.screens.add_edit_note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.ui.component.BaseDialog
import dev.carlodips.notes_compose.ui.screens.add_edit_note.dropdown.EditNoteDropdownMenu
import dev.carlodips.notes_compose.ui.screens.add_edit_note.util.AddEditNoteResultEvent
import dev.carlodips.notes_compose.ui.screens.add_edit_note.util.AddEditNoteUiEvent
import dev.carlodips.notes_compose.utils.ScreenMode
import kotlinx.coroutines.flow.collectLatest

// TODO:
//  3. add option to set reminder
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    modifier: Modifier = Modifier,
    onPopBackStack: (message: String?) -> Unit,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )
    var showUndoDialog by remember { mutableStateOf(false) }

    // Auto focus onStart only during Add Notes
    LaunchedEffect(uiState.value.shouldAutoFocusBody) {
        if (uiState.value.shouldAutoFocusBody) {
            focusRequester.requestFocus()
            viewModel.onAddEditNoteEvent(AddEditNoteUiEvent.AutoFocusedBody)
        }
    }

    // Behavior when back button is triggered.
    BackHandler(enabled = true) {
        when (uiState.value.screenMode) {
            ScreenMode.VIEW -> {
                onPopBackStack.invoke("")
            }

            ScreenMode.ADD, ScreenMode.EDIT -> {
                viewModel.onAddEditNoteEvent(AddEditNoteUiEvent.SaveNote)
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        /*Text(
                            if (uiState.value.isEdit) {
                                stringResource(id = R.string.edit_note)
                            } else {
                                stringResource(id = R.string.add_note)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )*/
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO: Show dialog kung gusto save changes*/ }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        if (uiState.value.screenMode == ScreenMode.EDIT) {
                            IconButton(onClick = {
                                showUndoDialog = !showUndoDialog
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_undo),
                                    contentDescription = ""
                                )
                            }
                        }

                        if (uiState.value.screenMode != ScreenMode.VIEW) {
                            IconButton(onClick = {
                                viewModel.onAddEditNoteEvent(AddEditNoteUiEvent.SaveNote)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null
                                )
                            }
                        } else {
                            EditNoteDropdownMenu(
                                onHide = {
                                    // TODO: Add hide functionality
                                },
                                onDelete = {
                                    focusManager.clearFocus()
                                    viewModel.onAddEditNoteEvent(AddEditNoteUiEvent.DeleteNote)
                                }
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(
                            R.string.display_last_edited,
                            uiState.value.lastEdited
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    /*IconButton(onClick = { *//*TODO*//* }) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                    }*/
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (uiState.value.screenMode == ScreenMode.VIEW && it.hasFocus) {
                                viewModel.setScreenMode(screenMode = ScreenMode.EDIT)
                            }
                        },
                    value = uiState.value.title,
                    onValueChange = {
                        viewModel.onAddEditNoteEvent(AddEditNoteUiEvent.EnteredTitle(it))
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.title))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (uiState.value.screenMode == ScreenMode.VIEW && it.hasFocus) {
                                viewModel.setScreenMode(screenMode = ScreenMode.EDIT)
                            }
                        },
                    value = uiState.value.body,
                    onValueChange = {
                        viewModel.onAddEditNoteEvent(AddEditNoteUiEvent.EnteredBody(it))
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.description))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    ),
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
            }
        }

        if (showUndoDialog) {
            BaseDialog(
                setShowDialog = { showUndoDialog = it },
                title = stringResource(id = R.string.undo),
                message = stringResource(R.string.dialog_msg_undo),
                onPositiveClick = {
                    focusManager.clearFocus()
                    viewModel.onAddEditNoteEvent(AddEditNoteUiEvent.UndoChanges)
                },
            )
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteResultEvent.NoteDeleted -> {
                    onPopBackStack.invoke(context.getString(R.string.msg_note_deleted))
                }

                is AddEditNoteResultEvent.NoteSaved -> {
                    focusManager.clearFocus()
                }

                is AddEditNoteResultEvent.NoteDiscarded -> {
                    val message = if (event.isEdit) {
                        ""
                    }
                    else {
                        context.getString(R.string.msg_note_discarded)
                    }

                    onPopBackStack.invoke(message)
                }
            }
        }
    }
}
