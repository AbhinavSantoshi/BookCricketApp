package com.example.bookcricketapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcricketapp.R
import com.example.bookcricketapp.utils.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    // Get UI scaling utilities
    val uiScale = rememberUiScaleUtils()
    
    // Animation states
    var showCricketBall by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    
    // Ball animation values
    val ballScale = animateFloatAsState(
        targetValue = if (showCricketBall) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    
    val ballRotation = animateFloatAsState(
        targetValue = if (showCricketBall) 720f else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = EaseOutQuart
        )
    )
    
    // Title animation
    val titleAlpha = animateFloatAsState(
        targetValue = if (showTitle) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = LinearEasing
        )
    )
    
    val titleScale = animateFloatAsState(
        targetValue = if (showTitle) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    // Tagline animation
    val taglineAlpha = animateFloatAsState(
        targetValue = if (showTagline) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = LinearEasing
        )
    )
    
    // Background gradient
    val pitchGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    )
    
    // Launch animation sequence
    LaunchedEffect(Unit) {
        showCricketBall = true
        delay(1000)
        showTitle = true
        delay(600)
        showTagline = true
        delay(1500)
        onNavigateToHome()
    }
    
    // Main layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = pitchGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Cricket ball with animation
            Box(
                modifier = Modifier
                    .scaledSize(160.dp)
                    .graphicsLayer {
                        scaleX = ballScale.value
                        scaleY = ballScale.value
                        rotationY = ballRotation.value
                    }
                    .shadow(
                        elevation = uiScale.scaledDp(8.dp),
                        shape = CircleShape,
                        spotColor = MaterialTheme.colorScheme.primary
                    )
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                // Red cricket ball with seam
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scaledPadding(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                    // Ball seam
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .scaledWidth(100.dp)
                            .scaledHeight(8.dp)
                            .graphicsLayer {
                                rotationZ = 30f
                            }
                            .background(Color.White)
                    )
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .scaledWidth(100.dp)
                            .scaledHeight(8.dp)
                            .graphicsLayer {
                                rotationZ = -30f
                            }
                            .background(Color.White)
                    )
                }
            }
            
            Spacer(modifier = Modifier.scaledHeight(48.dp))
            
            // Title animation
            ScaledText(
                text = "BOOK CRICKET",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = titleAlpha.value
                        scaleX = titleScale.value
                        scaleY = titleScale.value
                    }
            )
            
            Spacer(modifier = Modifier.scaledHeight(16.dp))
            
            // Tagline with fade-in animation
            ScaledText(
                text = "The Classic Game, Reimagined",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }
        
        // Version text at bottom
        ScaledText(
            text = "v1.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .scaledPadding(bottom = 16.dp)
                .alpha(taglineAlpha.value)
        )
    }
}