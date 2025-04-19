package io.cricket.bookcricketapp.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.cricket.bookcricketapp.utils.*

@Composable
fun CricketButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonType: ButtonType = ButtonType.PRIMARY,
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val uiScale = rememberUiScaleUtils()
    val layoutDir = LocalLayoutDirection.current
    
    // Enhanced scale animation when pressed for better feedback
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = if (isPressed) Spring.StiffnessHigh else Spring.StiffnessLow
        )
    )
    
    // Add a subtle rotation animation for playfulness
    val rotation by animateFloatAsState(
        targetValue = if (isPressed) -0.5f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    // Define gradient colors based on button type for a more vibrant look
    val gradientColors = when (buttonType) {
        ButtonType.PRIMARY -> listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        ButtonType.SECONDARY -> listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
        )
        ButtonType.TERTIARY -> listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
        )
        ButtonType.OUTLINE -> listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surface
        )
    }
    
    // Button colors based on type
    val colors = when (buttonType) {
        ButtonType.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
        )
        ButtonType.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
            disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
        )
        ButtonType.TERTIARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
            disabledContentColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.6f)
        )
        ButtonType.OUTLINE -> ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
    }
    
    // Enhanced elevation effect with scaled values
    val elevation = when (buttonType) {
        ButtonType.OUTLINE -> ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
        else -> ButtonDefaults.buttonElevation(
            defaultElevation = uiScale.scaledDp(6.dp),
            pressedElevation = uiScale.scaledDp(2.dp),
            disabledElevation = 0.dp
        )
    }
    
    Box(
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotation
            }
            .scale(scale)
    ) {
        when (buttonType) {
            ButtonType.OUTLINE -> {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .scaledHeight(48.dp)
                        .fillMaxWidth(),
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    elevation = elevation,
                    colors = colors as ButtonColors,
                    contentPadding = PaddingValues(
                        horizontal = uiScale.scaledDp(contentPadding.calculateLeftPadding(layoutDir)),
                        vertical = uiScale.scaledDp(contentPadding.calculateTopPadding())
                    ),
                    content = content
                )
            }
            else -> {
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .scaledHeight(48.dp)
                        .fillMaxWidth(),
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    elevation = elevation,
                    colors = colors,
                    contentPadding = PaddingValues(
                        horizontal = uiScale.scaledDp(contentPadding.calculateLeftPadding(layoutDir)),
                        vertical = uiScale.scaledDp(contentPadding.calculateTopPadding())
                    ),
                    content = content
                )
            }
        }
    }
}

@Composable
fun CricketText(
    text: String,
    style: TextStyle = MaterialTheme.typography.titleSmall,
    fontWeight: FontWeight = FontWeight.Medium,
    color: Color = Color.Unspecified
) {
    val uiScale = rememberUiScaleUtils()
    
    ScaledText(
        text = text,
        style = style,
        fontWeight = fontWeight,
        color = color
    )
}

enum class ButtonType {
    PRIMARY,
    SECONDARY,
    TERTIARY,
    OUTLINE
}