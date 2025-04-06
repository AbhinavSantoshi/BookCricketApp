package com.example.bookcricketapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcricketapp.ui.theme.ScoreboardStyle
import kotlinx.coroutines.delay

@Composable
fun ScoreboardCard(
    teamName: String,
    score: Int,
    wickets: Int,
    overs: String,
    maxWickets: Int,
    infoText: String? = null,
    modifier: Modifier = Modifier,
    isAnimated: Boolean = false
) {
    // Animation for score changes
    var scoreScale by remember { mutableStateOf(1f) }
    
    // If animated, trigger the scale animation when score changes
    LaunchedEffect(key1 = if (isAnimated) score else 0) {
        if (isAnimated) {
            scoreScale = 1.3f
            delay(150)
            scoreScale = 1f
        }
    }
    
    // Animated score scale
    val scoreScaleAnimated by animateFloatAsState(
        targetValue = scoreScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    // Create a gradient for the card
    val cardGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        )
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Use transparent to show gradient
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Team name with divider
                Text(
                    text = teamName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Score display
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = score.toString(),
                        style = ScoreboardStyle,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(scoreScaleAnimated)
                    )
                    
                    Text(
                        text = "/$wickets",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 3.dp, start = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Overs info
                Text(
                    text = "($overs)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                // Optional info text (like target)
                AnimatedVisibility(
                    visible = infoText != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    if (infoText != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = infoText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // Progress bar for wickets with gradient colors
                val wicketProgressColor = when {
                    wickets < maxWickets / 3 -> MaterialTheme.colorScheme.primary
                    wickets < maxWickets * 2/3 -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.error
                }
                
                LinearProgressIndicator(
                    progress = wickets.toFloat() / maxWickets,
                    color = wicketProgressColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Wickets: $wickets/$maxWickets",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun RunDisplay(
    runs: Int?,
    isOut: Boolean = false,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isOut -> MaterialTheme.colorScheme.errorContainer
        runs == 4 -> MaterialTheme.colorScheme.secondaryContainer
        runs == 6 -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val textColor = when {
        isOut -> MaterialTheme.colorScheme.onErrorContainer
        runs == 4 -> MaterialTheme.colorScheme.onSecondaryContainer
        runs == 6 -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    // Add a pulsating animation effect for boundaries and wickets
    val pulseAnimation = remember { Animatable(1f) }
    
    LaunchedEffect(runs, isOut) {
        if (runs == 4 || runs == 6 || isOut) {
            pulseAnimation.animateTo(
                targetValue = 1.2f,
                animationSpec = repeatable(
                    iterations = 3,
                    animation = tween(200),
                    repeatMode = RepeatMode.Reverse
                )
            )
            pulseAnimation.animateTo(1f)
        }
    }
    
    Box(
        modifier = modifier
            .size(64.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .scale(pulseAnimation.value),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isOut) "OUT" else runs?.toString() ?: "-",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    }
}