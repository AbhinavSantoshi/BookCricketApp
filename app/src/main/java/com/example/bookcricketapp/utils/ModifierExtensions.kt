package com.example.bookcricketapp.utils

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Extension functions for Modifier to easily apply scaling
 */

/**
 * Applies scaled padding to all sides
 */
@Composable
fun Modifier.scaledPadding(all: Dp): Modifier {
    val scale = rememberUiScaleUtils()
    return this.padding(scale.scaledDp(all))
}

/**
 * Applies scaled padding to individual sides
 */
@Composable
fun Modifier.scaledPadding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
): Modifier {
    val scale = rememberUiScaleUtils()
    return this.padding(
        start = scale.scaledDp(start),
        top = scale.scaledDp(top),
        end = scale.scaledDp(end),
        bottom = scale.scaledDp(bottom)
    )
}

/**
 * Applies scaled padding to horizontal and vertical directions
 */
@Composable
fun Modifier.scaledPadding(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp
): Modifier {
    val scale = rememberUiScaleUtils()
    return this.padding(
        horizontal = scale.scaleWidth(horizontal),
        vertical = scale.scaleHeight(vertical)
    )
}

/**
 * Applies scaled size (both width and height)
 */
@Composable
fun Modifier.scaledSize(size: Dp): Modifier {
    val scale = rememberUiScaleUtils()
    return this.size(scale.scaledDp(size))
}

/**
 * Applies scaled width
 */
@Composable
fun Modifier.scaledWidth(width: Dp): Modifier {
    val scale = rememberUiScaleUtils()
    return this.width(scale.scaleWidth(width))
}

/**
 * Applies scaled height
 */
@Composable
fun Modifier.scaledHeight(height: Dp): Modifier {
    val scale = rememberUiScaleUtils()
    return this.height(scale.scaleHeight(height))
}