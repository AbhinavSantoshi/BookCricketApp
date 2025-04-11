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
    primary = ScoreboardBlue,
    onPrimary = Color.White,
    primaryContainer = DeepBlue,
    onPrimaryContainer = Color(0xFF002039),
    
    secondary = PitchGreen,
    onSecondary = Color.White,
    secondaryContainer = BrightGreen,
    onSecondaryContainer = Color(0xFF002200),
    
    tertiary = CricketOrange,
    onTertiary = Color.White,
    tertiaryContainer = LightWood,
    onTertiaryContainer = Color(0xFF3F1500),
    
    error = CricketRed,
    errorContainer = Color(0xFFFFDAD6),
    onError = Color.White,
    onErrorContainer = Color(0xFF410002),
    
    background = LightBackground,
    onBackground = Color(0xFF1A1C19),
    surface = LightSurface,
    onSurface = Color(0xFF1A1C19),
    
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF43483F),
    outline = Color(0xFF73796E)
)

private val DarkColorScheme = darkColorScheme(
    primary = ScoreboardBlue,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF004A77),
    onPrimaryContainer = DeepBlue,
    
    secondary = PitchGreen,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF005313),
    onSecondaryContainer = BrightGreen,
    
    tertiary = CricketOrange,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF5D3F35),
    onTertiaryContainer = LightWood,
    
    error = CricketRed,
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = DarkBackground,
    onBackground = Color(0xFFE2E3DD),
    surface = DarkSurface,
    onSurface = Color(0xFFE2E3DD),
    
    surfaceVariant = Color(0xFF43483F),
    onSurfaceVariant = Color(0xFFC3C8BC),
    outline = Color(0xFF8D9387)
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