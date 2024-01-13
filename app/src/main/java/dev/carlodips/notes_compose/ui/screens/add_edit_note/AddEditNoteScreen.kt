package dev.carlodips.notes_compose.ui.screens.add_edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.carlodips.notes_compose.R

@Composable
fun AddEditNoteScreen(
    modifier: Modifier = Modifier,
    onPopBackStack: () -> Unit,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value.isDoneSaving) {
        if (uiState.value.isDoneSaving) {
            onPopBackStack.invoke()
            // uiState.value.isDoneSaving does not need to reset to false since
            // this vm will be destroyed anyways after popBackStack
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.onSaveNoteClick()
                }) {
                    Icon(Icons.Filled.Check, "Save")
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (uiState.value.isEdit) {
                        stringResource(id = R.string.edit_note)
                    } else {
                        stringResource(id = R.string.add_note)
                    },
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.value.title,
                    onValueChange = {
                        viewModel.onTitleChange(it)
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.title))
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.value.body,
                    onValueChange = {
                        viewModel.onBodyChange(it)
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.description))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )
            }
        }
    }
}
