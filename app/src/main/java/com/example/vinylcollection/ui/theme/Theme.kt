package com.example.vinylcollection.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Gold,
    secondary = Sand,
    background = Cream,
    surface = Cream,
    onPrimary = Brown,
    onSecondary = Brown,
    onBackground = Brown,
    onSurface = Brown
)

@Composable
fun VinylCollectionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
