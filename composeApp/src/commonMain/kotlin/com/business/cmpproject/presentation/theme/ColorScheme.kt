package com.business.cmpproject.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightAppColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    background = CreamBackground,
    surface = CreamSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary
)

val DarkAppColorScheme = darkColorScheme(
    primary = PinkPrimary,
    secondary = PinkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary
)