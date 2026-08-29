package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PurpleBg,
    onPrimaryContainer = PurpleDark,
    secondary = PurpleLight,
    onSecondary = Color.White,
    tertiary = GoldAccent,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = SurfaceCard,
    onSurface = TextDark,
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = TextGray,
    outline = DividerColor,
)

private val DarkColorScheme = darkColorScheme(
    primary = PurpleLight,
    onPrimary = Color.White,
    primaryContainer = PurpleDark,
    onPrimaryContainer = PurpleBg,
    secondary = PurplePrimary,
    background = Color(0xFF121218),
    surface = Color(0xFF1E1B2E),
    onBackground = Color(0xFFF3F4F6),
    onSurface = Color(0xFFF3F4F6)
)

@Composable
fun MMPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = PurpleDark.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
