package io.cricket.bookcricketapp.utils

import android.content.Context
import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * A class that provides scaling factors for UI components based on screen dimensions.
 */
class UiScaleUtils(
    val screenWidthDp: Float,
    val screenHeightDp: Float,
    private val referenceWidth: Float = 393f, // Reference design width (typical medium sized phone)
    private val referenceHeight: Float = 873f // Reference design height (typical medium sized phone)
) {
    // Calculate scale factors based on screen dimensions
    val horizontalScale: Float = screenWidthDp / referenceWidth
    val verticalScale: Float = screenHeightDp / referenceHeight
    
    // Use the minimum scale to ensure content fits without exceeding screen boundaries
    val uniformScale: Float = min(horizontalScale, verticalScale)
    
    // Scale for components that should scale differently in each dimension
    fun scaledDp(dp: Dp): Dp = (dp.value * uniformScale).dp
    
    // Scale specifically for horizontal dimensions
    fun scaleWidth(dp: Dp): Dp = (dp.value * horizontalScale).dp
    
    // Scale specifically for vertical dimensions
    fun scaleHeight(dp: Dp): Dp = (dp.value * verticalScale).dp
    
    // Scale font size to keep text readable on all devices
    fun scaledSp(sp: Float): Float {
        // Limit the scaling for text to avoid making it too small or too large
        val limitedScale = uniformScale.coerceIn(0.85f, 1.15f)
        return sp * limitedScale
    }
    
    companion object {
        // Get device metrics for native Android calculations
        fun fromContext(context: Context): UiScaleUtils {
            val displayMetrics = context.resources.displayMetrics
            val widthDp = displayMetrics.widthPixels / displayMetrics.density
            val heightDp = displayMetrics.heightPixels / displayMetrics.density
            return UiScaleUtils(widthDp, heightDp)
        }
    }
}

// CompositionLocal for making UiScaleUtils accessible throughout the app
val LocalUiScaleUtils = compositionLocalOf<UiScaleUtils> { 
    error("UiScaleUtils not provided")
}

/**
 * Composition function to provide UiScaleUtils to all child composables
 */
@Composable
fun ProvideUiScaleUtils(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    val screenWidthDp = with(density) { configuration.screenWidthDp.toFloat() }
    val screenHeightDp = with(density) { configuration.screenHeightDp.toFloat() }
    
    val uiScaleUtils = remember(screenWidthDp, screenHeightDp) {
        UiScaleUtils(screenWidthDp, screenHeightDp)
    }
    
    CompositionLocalProvider(LocalUiScaleUtils provides uiScaleUtils) {
        content()
    }
}

/**
 * Helper function to use UiScaleUtils within composables
 */
@Composable
fun rememberUiScaleUtils(): UiScaleUtils {
    return LocalUiScaleUtils.current
}