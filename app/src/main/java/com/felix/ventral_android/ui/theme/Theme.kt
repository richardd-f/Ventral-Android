package com.felix.ventral_android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val DefaultColorScheme = lightColorScheme(
    primary = AccentPurple,
    background = DarkPurple,
    secondary = DarkPurpleSecondary,
    surface = Color(0xFF251A3D), // Used for Cards
    onPrimary = PureWhite,
    onBackground = PureWhite,
    onSurface = LightPurple,
    error = ErrorRed
)

@Composable
fun Ventral_AndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = DefaultColorScheme,
        typography = Typography,
        content = content
    )
}