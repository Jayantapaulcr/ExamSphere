package com.noveletytech.examsphere.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A3B8E), // Default primary
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun ExamSphereTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Allow overriding the primary color remotely
    primaryColor: Color? = null,
    dynamicColor: Boolean = false, // Set to false to prioritize remote primary color
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> {
            if (primaryColor != null) DarkColorScheme.copy(primary = primaryColor) else DarkColorScheme
        }
        else -> {
            if (primaryColor != null) LightColorScheme.copy(primary = primaryColor) else LightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
