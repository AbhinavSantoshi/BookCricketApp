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
    primary = VibrantGreen,
    onPrimary = Color.White,
    primaryContainer = BrightGreen,
    onPrimaryContainer = Color(0xFF002200),
    
    secondary = CricketOrange,
    onSecondary = Color.White,
    secondaryContainer = LightWood,
    onSecondaryContainer = Color(0xFF3F1500),
    
    tertiary = VibrantBlue,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD3E4FF),
    onTertiaryContainer = Color(0xFF001934),
    
    error = CricketRed,
    errorContainer = Color(0xFFFFDAD6),
    onError = Color.White,
    onErrorContainer = Color(0xFF410002),
    
    background = Color(0xFFFFFDF5),
    onBackground = Color(0xFF1A1C19),
    surface = Color.White,
    onSurface = Color(0xFF1A1C19),
    
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF43483F),
    outline = Color(0xFF73796E)
)

private val DarkColorScheme = darkColorScheme(
    primary = VibrantGreen,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF005313),
    onPrimaryContainer = BrightGreen,
    
    secondary = CricketOrange,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF5D3F35),
    onSecondaryContainer = LightWood,
    
    tertiary = VibrantBlue,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF004A77),
    onTertiaryContainer = DeepBlue,
    
    error = CricketRed,
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = Color(0xFF121212),
    onBackground = Color(0xFFE2E3DD),
    surface = Color(0xFF1A1C19),
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