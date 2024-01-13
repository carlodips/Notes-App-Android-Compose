package dev.carlodips.notes_compose.ui.screens.notes_list

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.data.local.entity.Note
import dev.carlodips.notes_compose.ui.component.BaseCard
import dev.carlodips.notes_compose.utils.NavigationItem

@Composable
fun NotesListScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel = hiltViewModel(),
    onNavigateToAddEdit: (route: String) -> Unit,
) {
    val notesList = viewModel.notesList.collectAsState(initial = emptyList())

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
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

                items(notesList.value) { note ->
                    NoteItem(
                        note = note,
                        onNoteClick = {
                            onNavigateToAddEdit.invoke(
                                NavigationItem.AddEditNote.route + "?noteId=${note.noteId}"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClick: () -> Unit
) {
    BaseCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        onClick = onNoteClick,
        isEnabled = true
    ) {
        Column(
            modifier = modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {
            Text(
                text = note.noteTitle,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.noteBody,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
