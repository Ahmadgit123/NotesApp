package com.ahmadziya.notesapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary         = Primary,
    secondary       = Secondary,
    background      = Background,
    surface         = Surface,
    onPrimary       = OnPrimary,
    onBackground    = OnBackground,
    onSurface       = OnBackground,
)

@Composable
fun NotesAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
