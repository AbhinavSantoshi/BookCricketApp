package com.example.bookcricketapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookcricketapp.R
import com.example.bookcricketapp.components.ButtonType
import com.example.bookcricketapp.components.CricketButton
import com.example.bookcricketapp.components.CricketText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Data class to store ball animation properties
data class FloatingBall(
    val initialX: Float,
    val initialY: Float,
    val size: Float,
    val color: Color
)

@Composable
fun HomeScreen(
    onNewGameClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    
    // Animation states
    var showContent by remember { mutableStateOf(false) }
    var showFloatingBalls by remember { mutableStateOf(false) }
    
    // Background gradient with more vibrant colors
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surface
        )
    )
    
    // Ball rotation animation for decorative effect
    val ballRotation = rememberInfiniteTransition()
    val rotationAngle by ballRotation.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Define colors outside of the remember block
    val primaryColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val secondaryColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    val tertiaryColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
    val containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)

    // Create floating ball data
    val floatingBallsData = remember {
        List(5) {
            FloatingBall(
                initialX = (-150..150).random().toFloat(),
                initialY = (-300..300).random().toFloat(),
                size = (15..30).random().toFloat(),
                color = when ((0..3).random()) {
                    0 -> primaryColor
                    1 -> secondaryColor
                    2 -> tertiaryColor
                    else -> containerColor
                }
            )
        }
    }
    
    // Create animatable values for each ball - separated from the data to avoid @Composable invocation issues
    val xOffsets = remember {
        List(5) { Animatable(floatingBallsData[it].initialX) }
    }
    
    val yOffsets = remember {
        List(5) { Animatable(floatingBallsData[it].initialY) }
    }

    // Animate floating balls
    LaunchedEffect(showFloatingBalls) {
        if (showFloatingBalls) {
            for (i in 0 until 5) {
                launch {
                    while (true) {
                        xOffsets[i].animateTo(
                            targetValue = (-150..150).random().toFloat(),
                            animationSpec = tween(
                                durationMillis = (5000..8000).random(),
                                easing = LinearEasing
                            )
                        )
                    }
                }
                launch {
                    while (true) {
                        yOffsets[i].animateTo(
                            targetValue = (-300..300).random().toFloat(),
                            animationSpec = tween(
                                durationMillis = (6000..10000).random(),
                                easing = LinearEasing
                            )
                        )
                    }
                }
            }
        }
    }
    
    // Launch animations sequentially
    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
        delay(500)
        showFloatingBalls = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        // Cricket field decorative image
        Image(
            painter = painterResource(id = R.drawable.cricket_field),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .alpha(0.15f)
        )

        // Floating decorative cricket balls
        if (showFloatingBalls) {
            for (i in 0 until 5) {
                Box(
                    modifier = Modifier
                        .offset(x = xOffsets[i].value.dp, y = yOffsets[i].value.dp)
                        .size(floatingBallsData[i].size.dp)
                        .clip(CircleShape)
                        .background(floatingBallsData[i].color)
                        .align(Alignment.Center)
                )
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section with logo and title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // App logo - cricket ball and bat with enhanced styling
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .shadow(8.dp, CircleShape)
                                .graphicsLayer {
                                    rotationZ = rotationAngle
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            // Cricket ball - more vibrant
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .shadow(8.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiary)
                            ) {
                                // Ball seam with better contrast
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .width(70.dp)
                                        .height(6.dp)
                                        .graphicsLayer {
                                            rotationZ = 30f
                                        }
                                        .background(Color.White)
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .width(70.dp)
                                        .height(6.dp)
                                        .graphicsLayer {
                                            rotationZ = -30f
                                        }
                                        .background(Color.White)
                                )
                            }
                            
                            // Cricket bat (positioned to the right of the ball) - enhanced design
                            Box(
                                modifier = Modifier
                                    .size(110.dp, 190.dp)
                                    .graphicsLayer {
                                        translationX = 70f
                                        rotationZ = 45f
                                    }
                            ) {
                                // Bat handle with more contrast
                                Box(
                                    modifier = Modifier
                                        .width(18.dp)
                                        .height(90.dp)
                                        .align(Alignment.TopCenter)
                                        .shadow(4.dp, RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.secondary)
                                )
                                
                                // Bat blade with better shadows and shape
                                Box(
                                    modifier = Modifier
                                        .width(45.dp)
                                        .height(110.dp)
                                        .align(Alignment.BottomCenter)
                                        .shadow(6.dp, RoundedCornerShape(8.dp))
                                        .background(Color.LightGray)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(28.dp))
                        
                        // App title - larger and more vibrant
                        Text(
                            text = "BOOK CRICKET",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // App tagline with better contrast
                        Text(
                            text = "The Classic Game, Reimagined",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            // Bottom section with buttons - enhanced with animations
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { 100 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    )
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CricketButton(
                            onClick = onNewGameClick,
                            buttonType = ButtonType.PRIMARY,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            CricketText(
                                text = "New Game",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        CricketButton(
                            onClick = onAboutClick,
                            buttonType = ButtonType.SECONDARY,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            CricketText(
                                text = "How to Play",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            // Footer with version info and copyright
            Column(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "v1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "© Santoshi Software",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }
    }
}