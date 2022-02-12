package com.micudasoftware.linepicker.composeui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Primary,
    onPrimary = onPrimary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    surface = Surface,
    onSurface = onSurface,
    background = Background
)

private val LightColorPalette = lightColors(
    primary = Primary,
    onPrimary = onPrimary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    surface = Surface,
    onSurface = onSurface,
    background = Background

    /* Other default colors to override

    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun LinePickerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}