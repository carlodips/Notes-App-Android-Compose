package dev.carlodips.notes_compose.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.carlodips.notes_compose.ui.screens.add_edit_note.AddEditNoteScreen
import dev.carlodips.notes_compose.ui.screens.notes_list.NotesListScreen
import dev.carlodips.notes_compose.ui.screens.search.SearchScreen
import dev.carlodips.notes_compose.ui.theme.NotesComposeTheme
import dev.carlodips.notes_compose.utils.NavigationItem

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesComposeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavigationItem.NotesList.route
                ) {
                    composable(NavigationItem.NotesList.route) { entry ->
                        val messageFromAddEditScreen = entry.savedStateHandle.get<String>(
                            NavigationItem.NotesList.MESSAGE
                        ) ?: ""

                        NotesListScreen(
                            messageFromAddEdit = messageFromAddEditScreen,
                            onNavigateToAddEdit = {
                                navController.navigate(it)
                            },
                            onNavigateToSearch = {
                                navController.run {
                                    // Fix for snackbar showing when navigating back to NoteList
                                    currentBackStackEntry?.savedStateHandle?.remove<String>(
                                        NavigationItem.NotesList.MESSAGE
                                    )
                                    navigate(NavigationItem.Search.route)
                                }
                            }
                        )
                    }

                    composable(
                        route = NavigationItem.AddEditNote.route +
                                "?noteId={${NavigationItem.AddEditNote.NOTE_ID}}",
                        arguments = listOf(
                            navArgument(NavigationItem.AddEditNote.NOTE_ID) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddEditNoteScreen(onPopBackStack = { messageForSnackBar ->
                            // For displaying messages for Snackbar
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                NavigationItem.NotesList.MESSAGE,
                                messageForSnackBar
                            )
                            navController.popBackStack()
                        })
                    }

                    composable(
                        route = NavigationItem.Search.route
                    ) {
                        SearchScreen(
                            onPopBackStack = {
                                navController.popBackStack()
                            },
                            onNavigateToViewNote = {
                                navController.navigate(it)
                            }
                        )
                    }
                }
            }
        }
    }
}
