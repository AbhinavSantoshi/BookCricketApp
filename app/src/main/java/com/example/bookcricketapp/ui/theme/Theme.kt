package com.example.bookcricketapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.bookcricketapp.ui.theme.BookCricketTypography

private val LightColorScheme = lightColorScheme(
    primary = CricketBlue,
    onPrimary = Color.White,
    primaryContainer = LightBlue,
    onPrimaryContainer = DarkestBlue,
    
    secondary = PitchGreen,
    onSecondary = Color.White,
    secondaryContainer = LightGreen,
    onSecondaryContainer = DarkestGreen,
    
    tertiary = BoundaryOrange,
    onTertiary = Color.White,
    tertiaryContainer = BoundaryOrange.copy(alpha = 0.2f),
    onTertiaryContainer = Color(0xFF3F1500),
    
    error = ActionRed,
    errorContainer = ActionRed.copy(alpha = 0.2f),
    onError = Color.White,
    onErrorContainer = Color(0xFF410002),
    
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,
    outline = MediumGray
)

private val DarkColorScheme = darkColorScheme(
    primary = CricketBlue,
    onPrimary = Color.Black,
    primaryContainer = DeepBlue,
    onPrimaryContainer = LightBlue,
    
    secondary = PitchGreen,
    onSecondary = Color.Black,
    secondaryContainer = DeepGreen,
    onSecondaryContainer = LightGreen,
    
    tertiary = BoundaryOrange,
    onTertiary = Color.Black,
    tertiaryContainer = BoundaryOrange.copy(alpha = 0.3f),
    onTertiaryContainer = BoundaryOrange.copy(alpha = 0.8f),
    
    error = ActionRed,
    errorContainer = ActionRed.copy(alpha = 0.3f),
    onError = Color.Black,
    onErrorContainer = ActionRed.copy(alpha = 0.8f),
    
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    
    surfaceVariant = VeryDarkGray,
    onSurfaceVariant = LightGray,
    outline = MediumGray
)

@Composable
fun BookCricketAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to false to use our vibrant custom colors
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BookCricketTypography,
        content = content
    )
}