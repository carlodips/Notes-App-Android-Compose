package dev.carlodips.notes_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.carlodips.notes_compose.ui.screens.add_edit_note.AddEditNoteScreen
import dev.carlodips.notes_compose.ui.screens.notes_list.NotesListScreen
import dev.carlodips.notes_compose.ui.theme.NotesComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesComposeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.NotesList.route
                ) {
                    composable(Screens.NotesList.route) {
                        NotesListScreen(onNavigateToAddEdit = {
                            navController.navigate(it)
                        })
                    }

                    composable(Screens.AddEditNote.route) {
                        AddEditNoteScreen(onPopBackStack = {
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}

sealed class Screens(val route: String) {
    object NotesList : Screens("notes_list")
    object AddEditNote : Screens("add_edit_note")
}
