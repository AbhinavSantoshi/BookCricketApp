package io.cricket.bookcricketapp.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically

/**
 * Animation utilities for consistent animation behavior across the app
 */

/**
 * Creates a standard slide-in from bottom animation
 * @param durationMillis Animation duration in milliseconds
 * @param offsetY Initial offset in pixels
 * @param delayMillis Optional delay before starting the animation
 */
fun standardSlideInBottom(
    durationMillis: Int = 300,
    offsetY: Int = 100, 
    delayMillis: Int = 0
): EnterTransition = fadeIn(
    animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis)
) + slideInVertically(
    animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis),
    initialOffsetY = { offsetY }
)

/**
 * Creates a bouncy slide-in from bottom animation
 * @param offsetY Initial offset in pixels
 */
fun bouncySlideInBottom(
    offsetY: Int = 100
): EnterTransition = fadeIn(
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
) + slideInVertically(
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    ),
    initialOffsetY = { offsetY }
)

/**
 * Creates a standard slide-in from top animation
 * @param durationMillis Animation duration in milliseconds
 * @param offsetY Initial offset in pixels (negative value for top)
 * @param delayMillis Optional delay before starting the animation
 */
fun standardSlideInTop(
    durationMillis: Int = 300,
    offsetY: Int = -100,
    delayMillis: Int = 0
): EnterTransition = fadeIn(
    animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis)
) + slideInVertically(
    animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis),
    initialOffsetY = { offsetY }
)