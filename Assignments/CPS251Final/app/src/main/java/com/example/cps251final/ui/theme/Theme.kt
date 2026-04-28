package com.example.cps251final.ui.theme

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
import com.example.cps251final.ui.theme.*
private val DarkColorScheme = darkColorScheme(
    primaryContainer = Color(0xFF381E72),
    onPrimaryContainer = Color(0xFFD0BCFF),

    errorContainer = Color(0xFF601410),
    onErrorContainer = Color(0xFFF2B8B5),

    secondaryContainer = Color(0xFF332D41),
    onSecondaryContainer = Color(0xFFE8DEF8),

    surface = Color(0xFF121212),
    onSurfaceVariant = Color(0xFFB0B0B0)
)

private val LightColorScheme = lightColorScheme(
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    errorContainer = ErrorContainer,
    onErrorContainer = onErrorContainer,

    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    surface = Surface,
    onSurfaceVariant = OnSurfaceVariant
)

@Composable
fun CPS251FinalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}