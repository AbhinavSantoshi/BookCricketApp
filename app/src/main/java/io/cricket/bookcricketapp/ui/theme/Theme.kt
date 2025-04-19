package io.cricket.bookcricketapp.ui.theme

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
    onPrimaryContainer = Color.White,  // Increased contrast
    
    secondary = PitchGreen,
    onSecondary = Color.Black,
    secondaryContainer = DeepGreen,
    onSecondaryContainer = Color.White,  // Increased contrast
    
    tertiary = BoundaryOrange,
    onTertiary = Color.Black,
    tertiaryContainer = BoundaryOrange.copy(alpha = 0.3f),
    onTertiaryContainer = Color.White,  // Increased contrast
    
    error = ActionRed,
    errorContainer = ActionRed.copy(alpha = 0.3f),
    onError = Color.Black,
    onErrorContainer = Color.White,  // Increased contrast
    
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = Color.White.copy(alpha = 0.85f),  // Brighter for better visibility
    
    surfaceVariant = VeryDarkGray,
    onSurfaceVariant = Color.White.copy(alpha = 0.7f),  // Much brighter for better visibility
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
            
            // Modern approach instead of deprecated statusBarColor
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
            }
            
            // Set the status bar background color
            window.decorView.setBackgroundColor(colorScheme.primary.toArgb())
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BookCricketTypography,
        content = content
    )
}