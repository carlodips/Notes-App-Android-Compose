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
                    composable(NavigationItem.NotesList.route) {
                        NotesListScreen(onNavigateToAddEdit = {
                            navController.navigate(it)
                        })
                    }

                    composable(
                        route = NavigationItem.AddEditNote.route + "?noteId={noteId}",
                        arguments = listOf(
                            navArgument("noteId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddEditNoteScreen(onPopBackStack = {
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}
