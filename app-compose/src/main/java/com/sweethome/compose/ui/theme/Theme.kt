package com.sweethome.compose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SweetHomeColors = lightColorScheme(
    primary = Color(0xFF464646),
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF464646),
    surface = Color.White,
    onSurface = Color(0xFF464646)
)

@Composable
fun SweetHomeComposeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SweetHomeColors,
        content = content
    )
}
