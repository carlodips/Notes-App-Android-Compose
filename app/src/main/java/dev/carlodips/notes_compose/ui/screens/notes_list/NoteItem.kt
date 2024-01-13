package dev.carlodips.notes_compose.ui.screens.notes_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.data.local.entity.Note
import dev.carlodips.notes_compose.ui.component.BaseCard

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    BaseCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
                    .weight(1f)
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = note.noteTitle
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = note.noteBody
                )
            }

            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopStart)
            ) {
                IconButton(onClick = { isVisible = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Settings"
                    )
                }
                DropdownMenu(modifier = modifier,
                    expanded = isVisible,
                    onDismissRequest = { isVisible = false },
                    content = {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    text = stringResource(id = R.string.edit_note)
                                )
                            },
                            onClick = {
                                onEditClick.invoke()
                                isVisible = !isVisible
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    text = stringResource(id = R.string.delete)
                                )
                            },
                            onClick = {
                                showDeleteDialog = !showDeleteDialog
                                isVisible = !isVisible
                            }
                        )

                    }
                )
            }
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
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                        Spacer(modifier = Modifier.width(16.dp))

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