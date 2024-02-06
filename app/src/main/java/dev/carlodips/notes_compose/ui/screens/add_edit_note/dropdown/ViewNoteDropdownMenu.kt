package dev.carlodips.notes_compose.ui.screens.add_edit_note.dropdown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.ui.component.BaseDialog

@Composable
fun ViewNoteDropdownMenu(
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var showArchiveDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var isVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { isVisible = true }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = null
            )
        }
        DropdownMenu(modifier = Modifier,
            expanded = isVisible,
            onDismissRequest = { isVisible = false },
            content = {
                DropdownMenuItem(
                    text = {
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = stringResource(R.string.archive)
                        )
                    },
                    onClick = {
                        showArchiveDialog = !showArchiveDialog
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

    if (showDeleteDialog) {
        BaseDialog(
            setShowDialog = { showDeleteDialog = it },
            title = stringResource(id = R.string.delete),
            message = stringResource(R.string.msg_delete),
            onPositiveClick = {
                onDelete.invoke()
            },
        )
    }

    if (showArchiveDialog) {
        BaseDialog(
            setShowDialog = { showArchiveDialog = it },
            title = stringResource(id = R.string.archive),
            message = stringResource(R.string.msg_archive_this_note),
            onPositiveClick = {
                onArchive.invoke()
            },
        )
    }
}
