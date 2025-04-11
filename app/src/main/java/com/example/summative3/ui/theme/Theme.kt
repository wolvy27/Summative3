package com.example.summative3.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CustomLightColorScheme = lightColorScheme(
    primary = DarkGrey,
    secondary = MediumGrey,
    tertiary = LightGrey,
    background = White,
    surface = LightGrey,
    onPrimary = White,
    onSecondary = White,
    onTertiary = DarkGrey,
    onBackground = DarkGrey,
    onSurface = DarkGrey,
)

@Composable
fun Summative3Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CustomLightColorScheme,
        typography = Typography,
        content = content
    )
}