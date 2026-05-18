package com.ahmadziya.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmadziya.notesapp.ui.screens.NotesScreen
import com.ahmadziya.notesapp.ui.screens.PostsScreen
import com.ahmadziya.notesapp.ui.theme.NotesAppTheme

// Bottom navigation screens
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Notes : Screen("notes", "My Notes", Icons.Default.Note)
    object Posts : Screen("posts", "API Posts", Icons.Default.List)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    // State to track the selected screen
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Notes) }

    val tabs = listOf(Screen.Notes, Screen.Posts)

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { screen ->
                    NavigationBarItem(
                        selected = selectedScreen == screen,
                        onClick  = { selectedScreen = screen },
                        icon     = {
                            Icon(
                                imageVector        = screen.icon,
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Display the selected screen
            when (selectedScreen) {
                Screen.Notes -> NotesScreen()
                Screen.Posts -> PostsScreen()
            }
        }
    }
}
