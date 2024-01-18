package dev.carlodips.notes_compose.ui.screens.notes_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.domain.model.Note
import dev.carlodips.notes_compose.ui.component.BaseCard
import dev.carlodips.notes_compose.ui.theme.NotesComposeTheme
import java.time.LocalDateTime

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    BaseCard(
        modifier = modifier.padding(horizontal = 16.dp),
        onClick = onEditClick,
        isEnabled = true
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = note.displayNoteTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = note.displayNoteBody,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.bodySmall,
                text = note.formattedDateUpdated,
                color = Color.Gray
            )
        }
    }

    if (showDeleteDialog) {
        DeleteEntryDialog(
            setShowDialog = { showDeleteDialog = it },
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
fun DeleteEntryDialog(
    setShowDialog: (Boolean) -> Unit,
    onDeleteClick: () -> Unit = {}
) {

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface {
            Box(contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.delete_this_note),
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 12.dp),
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    setShowDialog(false)
                                },
                            text = stringResource(R.string.no),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onDeleteClick.invoke()
                                    setShowDialog(false)
                                },
                            text = stringResource(R.string.yes),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNoteItem() {
    NotesComposeTheme {

        val notesList = arrayListOf(
            Note(
                noteId = 0,
                noteTitle = "Sample title",
                noteBody = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.",
                dateAdded = LocalDateTime.now(),
                dateUpdated = LocalDateTime.now()
            ),
            Note(
                noteId = 1,
                noteTitle = "",
                noteBody = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.",
                dateAdded = LocalDateTime.now(),
                dateUpdated = LocalDateTime.now()
            ),
            Note(
                noteId = 2,
                noteTitle = "Sample title empty body",
                noteBody = "",
                dateAdded = LocalDateTime.now(),
                dateUpdated = LocalDateTime.now()
            )
        )

        Surface(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Column {
                notesList.forEach {
                    NoteItem(
                        note = it,
                        onEditClick = {},
                        onDeleteClick = {}
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}