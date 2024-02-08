package dev.carlodips.notes_compose.ui.screens.folders.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.domain.model.Folder
import java.time.LocalDateTime

// Tutorial: https://foso.github.io/Jetpack-Compose-Playground/foundation/lazyrow/
@Composable
fun FolderLazyRow(
    modifier: Modifier = Modifier,
    list: List<Folder>?,
    onItemClick: (Folder) -> Unit,
    selectedFolderId: Int?
) {
    if (!list.isNullOrEmpty()) {
        LazyRow(modifier = modifier.wrapContentWidth()) {
            items(items = list, itemContent = { folder ->
                Spacer(modifier = Modifier.width(4.dp))
                FolderHorizontalItem(
                    folder = folder,
                    onItemClick = onItemClick,
                    isSelected = selectedFolderId == folder.folderId
                )
                Spacer(modifier = Modifier.width(4.dp))
            })
        }
    }
}

@Composable
fun FolderHorizontalItem(
    folder: Folder,
    onItemClick: (Folder) -> Unit,
    isSelected: Boolean
) {
    ElevatedButton(
        onClick = { onItemClick.invoke(folder) },
        colors = if (isSelected) {
            ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            ButtonDefaults.elevatedButtonColors()
        }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            text = folder.folderName
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun PreviewHorizontalList2() {
    val templateAddictions = listOf(
        Folder(folderName = "All", dateUpdated = LocalDateTime.now()),
        Folder(folderName = "Personal", dateUpdated = LocalDateTime.now()),
        Folder(folderName = "Work", dateUpdated = LocalDateTime.now())
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                FolderLazyRow(
                    modifier = Modifier
                        .weight(0.9f)
                        .padding(start = 16.dp, end = 8.dp),
                    list = templateAddictions,
                    onItemClick = {},
                    selectedFolderId = null
                )
            }

            IconButton(
                modifier = Modifier.weight(0.1f),
                onClick = {}) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_folder),
                    contentDescription = null
                )
            }
        }
    }
}
