package dev.carlodips.notes_compose.ui.screens.folders

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import dev.carlodips.notes_compose.R
import dev.carlodips.notes_compose.ui.screens.notes_list.NotesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderListScreen(
    modifier: Modifier = Modifier,
    onPopBackStack: () -> Unit,
    viewModel: NotesListViewModel = hiltViewModel(),
) {
    val folderList = viewModel.foldersList.collectAsState(initial = null)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            stringResource(R.string.label_folders)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onPopBackStack.invoke() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {

                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        ) { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                item {
                    Text(text = "Folders")
                }

                if (!folderList.value.isNullOrEmpty()) {
                    items(folderList.value!!) {
                        Text(text = it.folderName)
                    }
                }
            }
        }
    }
}