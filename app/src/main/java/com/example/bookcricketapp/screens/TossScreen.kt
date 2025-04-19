package com.example.bookcricketapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.bookcricketapp.R
import com.example.bookcricketapp.components.ButtonType
import com.example.bookcricketapp.components.CricketButton
import com.example.bookcricketapp.components.CricketText
import com.example.bookcricketapp.utils.*
import com.example.bookcricketapp.viewmodels.GameViewModel
import com.example.bookcricketapp.viewmodels.TossChoice
import com.example.bookcricketapp.viewmodels.TossResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// Confetti particle data class for the celebratory effect
private data class Confetti(
    val position: Pair<Float, Float>, 
    val color: Color, 
    val alpha: Float, 
    val size: Float, 
    val rotation: Float, 
    val speed: Float
)

// Custom cricket bat icon composable
@Composable
fun CricketBatIcon(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary
) {
    Image(
        painter = painterResource(id = R.drawable.cricket_bat),
        contentDescription = "Cricket Bat",
        modifier = modifier.scaledSize(24.dp),
        contentScale = ContentScale.Fit
    )
}

// Custom cricket ball icon composable
@Composable
fun CricketBallIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFAA0000) // Cricket ball red
) {
    Image(
        painter = painterResource(id = R.drawable.cricket_ball),
        contentDescription = "Cricket Ball",
        modifier = modifier.scaledSize(20.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun TossScreen(
    gameViewModel: GameViewModel,
    onTossComplete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val uiScale = rememberUiScaleUtils()
    val hapticFeedback = LocalHapticFeedback.current
    
    // States for toss animation and result
    var tossInProgress by remember { mutableStateOf(false) }
    var coinFlipAnimationFinished by remember { mutableStateOf(false) }
    var showCoinOptions by remember { mutableStateOf(true) }
    var showInstructions by remember { mutableStateOf(true) }
    var showResult by remember { mutableStateOf(false) }
    
    // Track user choice and result
    var userSelectedHeads by remember { mutableStateOf(true) }
    var tossWon by remember { mutableStateOf(false) }
    var coinResult by remember { mutableStateOf<TossResult?>(null) }
    
    // State for batting/bowling choice
    var showBattingChoice by remember { mutableStateOf(false) }
    var battingChoiceMade by remember { mutableStateOf(false) }
    
    // Enhanced visual effects for coin flip
    val flipRotationY = remember { Animatable(0f) }
    val flipRotationX = remember { Animatable(0f) }  // Added X-axis rotation for wobble effect
    val coinElevation = remember { Animatable(4f) }
    val coinScale = remember { Animatable(1f) }
    val coinShine = remember { Animatable(0f) }  // For reflection/shine effect
    val wobble = remember { Animatable(0f) } // For additional wobble during flip
    
    // New animations for result reveal
    val resultCardScale = remember { Animatable(0.8f) }
    val resultCardElevation = remember { Animatable(2f) }
    var showConfetti by remember { mutableStateOf(false) }
    
    // Generate confetti particles when user wins the toss
    val confettiParticles = remember {
        mutableStateListOf<Confetti>().apply {
            if (tossWon) {
                repeat(60) {
                    add(
                        Confetti(
                            position = Pair(Random.nextFloat() * 1000f, -50f - Random.nextFloat() * 100),
                            color = listOf(
                                Color(0xFFFFD700), // Gold
                                Color(0xFF2196F3), // Blue 
                                Color(0xFF4CAF50), // Green
                                Color(0xFFE91E63), // Pink
                                Color(0xFF9C27B0), // Purple 
                                Color(0xFFFF9800)  // Orange
                            )[Random.nextInt(6)],
                            alpha = 0.9f + Random.nextFloat() * 0.1f, // Higher alpha for more vibrant colors
                            size = 10f + Random.nextFloat() * 18f,    // Slightly larger particles
                            rotation = Random.nextFloat() * 360f,
                            speed = 2f + Random.nextFloat() * 4f
                        )
                    )
                }
            }
        }
    }
    
    // Animation for confetti
    val confettiAnimatable = remember { Animatable(0f) }
    
    // Background gradient with cricket pitch-inspired colors that adapt to theme
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),    // Primary with low alpha at top
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f),   // Tertiary with low alpha in middle  
            MaterialTheme.colorScheme.surface                         // Surface color at bottom
        )
    )
    
    // Function to perform the coin toss animation with enhanced physics
    fun performToss() {
        coroutineScope.launch {
            showCoinOptions = false
            showInstructions = false
            tossInProgress = true
            
            // Launch simultaneous animations for better visual effect
            launch {
                // Initial coin animation (fly up with more natural curve)
                coinScale.animateTo(
                    targetValue = 1.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                
                // Coin elevation with improved physics
                coinElevation.animateTo(
                    targetValue = 30f,  // Higher elevation for more dramatic effect
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = CubicBezierEasing(0.2f, 0.0f, 0.8f, 1.0f) // More natural arc
                    )
                )
            }
            
            // Flipping animation with dynamic speed changes
            launch {
                // Initial fast flips with acceleration
                flipRotationY.animateTo(
                    targetValue = 1800f, // Multiple flips
                    animationSpec = keyframes {
                        durationMillis = 2000
                        // Start slower, accelerate in the middle, slow down at the end
                        0f at 0 with LinearEasing
                        900f at 800 with LinearEasing
                        1600f at 1600 with LinearEasing
                        1800f at 2000 with LinearEasing
                    }
                )
            }
            
            // X-axis wobble for more realistic physics
            launch {
                wobble.animateTo(
                    targetValue = 10f, 
                    animationSpec = repeatable(
                        iterations = 8,
                        animation = tween(250, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                wobble.animateTo(0f, animationSpec = tween(200))
            }
            
            // Add shine effect animation during flip
            launch {
                repeat(4) {
                    coinShine.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(300, easing = LinearEasing)
                    )
                    coinShine.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(300, easing = LinearEasing)
                    )
                }
            }
            
            // Allow time for animations to progress
            delay(1000)
                
            // Determine toss result
            val isHeads = Random.nextBoolean()
            coinResult = if (isHeads) TossResult.HEADS else TossResult.TAILS
            
            // First determine if player 1 (user) won the toss based on their choice
            tossWon = (userSelectedHeads && isHeads) || (!userSelectedHeads && !isHeads)
            
            // Set toss winner based on result - this determines who gets to choose batting/bowling
            if (gameViewModel.gameMode.name == "PVP") {
                // In PVP mode, toss should be determined by user's coin choice
                gameViewModel.tossWinner = if (tossWon) gameViewModel.team1Name else gameViewModel.team2Name
                // tossWon is already set correctly above - true if team1 won, false if team2 won
            } else {
                // In PVC mode, tossWon determines if player (team1) or computer (team2) won
                gameViewModel.tossWinner = if (tossWon) gameViewModel.team1Name else gameViewModel.team2Name
            }
            
            // If computer wins in PVC mode, it automatically chooses to bat
            if (!tossWon && gameViewModel.gameMode.name == "PVC") {
                gameViewModel.battingFirst = gameViewModel.team2Name
                gameViewModel.bowlingFirst = gameViewModel.team1Name
                // No need to show batting choice UI since computer has already chosen
                battingChoiceMade = true
            }
            
            // For PVP mode, regardless of who won, we'll show the batting choice UI
            // This way both Player A and Player B get to make a choice when they win
            
            // Wait for flip animations to complete
            delay(800)
            
            // Land with bounce effect
            launch {
                coinElevation.animateTo(
                    targetValue = 4f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            
            launch {
                coinScale.animateTo(
                    targetValue = 1.4f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
                
            // Finish animation
            coinFlipAnimationFinished = true
            if (gameViewModel.isHapticFeedbackEnabled) {
                try {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                } catch (e: Exception) {
                    // Safely handle any exceptions from haptic feedback
                }
            }
            delay(500)
            showResult = true
                
            // Animate result card appearance
            launch {
                resultCardScale.animateTo(
                    targetValue = 1.1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                resultCardScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(150, easing = LinearOutSlowInEasing)
                )
                
                resultCardElevation.animateTo(
                    targetValue = 8f,
                    animationSpec = tween(300)
                )
            }
            
            // Start confetti animation if player won the toss
            if (tossWon) {
                showConfetti = true
                launch {
                    confettiAnimatable.animateTo(
                        targetValue = 1000f,
                        animationSpec = tween(5000, easing = LinearEasing)
                    )
                }
            }
                
            // If player won the toss, show batting/bowling choice
            showBattingChoice = true // Always show some batting choice UI, but it will be conditional in the UI based on who won
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .scaledPadding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            AnimatedVisibility(
                visible = !tossInProgress || showResult,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                ScaledHeadlineSmall(
                    text = if (showResult) "Toss Result" else "Coin Toss",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scaledPadding(bottom = 16.dp)
                )
            }
            
            // Instructions
            AnimatedVisibility(
                visible = showInstructions,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(vertical = 16.dp),
                    shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = uiScale.scaledDp(2.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ScaledBodyMedium(
                            text = "Choose heads or tails for the toss",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.scaledHeight(8.dp))
                        
                        ScaledBodyMedium(
                            text = "Winner of the toss will choose to bat or bowl",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.scaledHeight(24.dp))
            
            // Enhanced coin animation area
            Box(
                modifier = Modifier
                    .scaledSize(200.dp)
                    .scaledPadding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Coin shadow (separate from coin to create more realistic effect)
                if (tossInProgress && coinElevation.value > 4f) {
                    Box(
                        modifier = Modifier
                            .scaledSize(100.dp * (2f - coinElevation.value/30f))  // Shadow gets smaller as coin goes higher
                            .clip(CircleShape)
                            .alpha(0.2f * (1f - coinElevation.value/30f))  // Shadow fades as coin goes higher
                            .background(Color.Black)
                    )
                }
                
                // The flipping coin - enhanced 3D effect
                Box(
                    modifier = Modifier
                        .scaledSize(120.dp)
                        .shadow(
                            elevation = uiScale.scaledDp(coinElevation.value.dp),
                            shape = CircleShape,
                            clip = true,
                            spotColor = Color(0xFFD4AF37) // Gold spot color for better shine
                        )
                        .graphicsLayer {
                            scaleX = coinScale.value
                            scaleY = coinScale.value
                            rotationY = flipRotationY.value
                            rotationX = wobble.value  // Add wobble for more realistic movement
                            cameraDistance = 16f * density  // Increased perspective for better 3D effect
                        }
                        .clip(CircleShape)
                        .border(
                            width = uiScale.scaledDp(4.dp),
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFD700).copy(alpha = if (coinShine.value > 0.5f) 0.9f else 0.7f), // Brighter gold
                                    Color(0xFFB8860B).copy(alpha = if (coinShine.value > 0.5f) 0.8f else 0.6f)  // Dark golden rod
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(100f, 100f)
                            ),
                            shape = CircleShape
                        )
                        .background(
                            brush = Brush.radialGradient(
                                colors = if ((coinResult == TossResult.HEADS && coinFlipAnimationFinished) || 
                                           (flipRotationY.value % 360 < 90 || flipRotationY.value % 360 > 270) && !coinFlipAnimationFinished) {
                                    // Heads side - adaptive colors for light/dark mode
                                    listOf(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),   // Base surface color
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)  // Surface variant for dimension
                                    )
                                } else {
                                    // Tails side - adaptive colors for light/dark mode
                                    listOf(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),  // Surface variant
                                        MaterialTheme.colorScheme.outline  // Outline color for better contrast
                                    )
                                },
                                center = Offset(
                                    x = 75f + 50f * coinShine.value, 
                                    y = 75f - 50f * coinShine.value
                                ),
                                radius = 180f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Show different sides based on rotation with fade effect during transition
                    val isHeadsSideVisible = if (coinFlipAnimationFinished) {
                        coinResult == TossResult.HEADS
                    } else {
                        flipRotationY.value % 360 < 90 || flipRotationY.value % 360 > 270
                    }
                    val transitionFactor = if (flipRotationY.value % 180 > 80 && flipRotationY.value % 180 < 100) {
                        // Create fade effect during transition
                        val progress = (flipRotationY.value % 180 - 80) / 20f
                        minOf(1f, maxOf(0f, progress))
                    } else {
                        1f
                    }
                    
                    // Heads side - with theme-adaptive colors
                    if (isHeadsSideVisible) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(transitionFactor),
                            contentAlignment = Alignment.Center
                        ) {
                            // Gold ring as part of the coin design - works in both light/dark
                            Box(
                                modifier = Modifier
                                    .scaledSize(90.dp)
                                    .border(
                                        width = uiScale.scaledDp(3.dp),
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFFFD700),  // Pure gold that works in any mode
                                                Color(0xFFFFC107)
                                            ),
                                            start = Offset(0f, 0f),
                                            end = Offset(100f, 100f)
                                        ),
                                        shape = CircleShape
                                    )
                            )
                            
                            ScaledText(
                                text = "H",
                                fontSize = scaledSp(48f),
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface, // Uses theme color for better contrast
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Tails side - with theme-adaptive colors
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(transitionFactor),
                            contentAlignment = Alignment.Center
                        ) {
                            // Gold ring as part of the coin design - works in both light/dark
                            Box(
                                modifier = Modifier
                                    .scaledSize(90.dp)
                                    .border(
                                        width = uiScale.scaledDp(3.dp),
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFFFD700),  // Pure gold that works in any mode
                                                Color(0xFFFFC107)
                                            ),
                                            start = Offset(0f, 0f),
                                            end = Offset(100f, 100f)
                                        ),
                                        shape = CircleShape
                                    )
                            )
                            
                            ScaledText(
                                text = "T",
                                fontSize = scaledSp(48f),
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface, // Uses theme color for better contrast
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    
                    // Shine effect overlay
                    if (coinShine.value > 0.1f) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.4f * coinShine.value),
                                            Color.Transparent
                                        ),
                                        center = Offset(30f, 30f),
                                        radius = 50f
                                    )
                                )
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.scaledHeight(24.dp))
            
            // Coin choice buttons (Heads/Tails)
            AnimatedVisibility(
                visible = showCoinOptions,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Heads button
                    CricketButton(
                        onClick = {
                            userSelectedHeads = true
                            performToss()
                        },
                        buttonType = ButtonType.PRIMARY,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
                    ) {
                        CricketText(
                            text = "Heads",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.scaledWidth(16.dp))
                    
                    // Tails button
                    CricketButton(
                        onClick = {
                            userSelectedHeads = false
                            performToss()
                        },
                        buttonType = ButtonType.SECONDARY,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
                    ) {
                        CricketText(
                            text = "Tails",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Result message
            AnimatedVisibility(
                visible = showResult,
                enter = fadeIn(animationSpec = tween(300)) + 
                        expandVertically(animationSpec = tween(500)),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(vertical = 16.dp)
                            .graphicsLayer {
                                scaleX = resultCardScale.value
                                scaleY = resultCardScale.value
                            }
                            .shadow(
                                elevation = uiScale.scaledDp(resultCardElevation.value.dp),
                                shape = RoundedCornerShape(uiScale.scaledDp(16.dp))
                            ),
                        shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (tossWon) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Improved "It's Heads/Tails" display
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Small decorative coin images
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = null,
                                    tint = if (tossWon) 
                                        MaterialTheme.colorScheme.onPrimaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.scaledSize(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.scaledWidth(8.dp))
                                
                                ScaledTitleLarge(
                                    text = "It's ${coinResult?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: ""}!",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (tossWon) 
                                        MaterialTheme.colorScheme.onPrimaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                
                                Spacer(modifier = Modifier.scaledWidth(8.dp))
                                
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = null,
                                    tint = if (tossWon) 
                                        MaterialTheme.colorScheme.onPrimaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.scaledSize(24.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.scaledHeight(12.dp))
                            
                            Divider(
                                color = if (tossWon)
                                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                modifier = Modifier.scaledPadding(vertical = 8.dp)
                            )
                            
                            Spacer(modifier = Modifier.scaledHeight(8.dp))
                            
                            // Improved winner announcement with bigger text and animation
                            ScaledHeadlineSmall(
                                text = if (tossWon) "You won the toss!" else "${gameViewModel.tossWinner} won the toss!",
                                fontWeight = FontWeight.Bold,
                                color = if (tossWon) 
                                    MaterialTheme.colorScheme.onPrimaryContainer 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            
                            // Only show batting/bowling choice message when a choice has been made
                            if (battingChoiceMade) {
                                Spacer(modifier = Modifier.scaledHeight(16.dp))
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    // Cricket bat/ball icon based on choice
                                    val chooseToBat = gameViewModel.battingFirst == gameViewModel.tossWinner
                                    Box(
                                        modifier = Modifier.scaledSize(20.dp)
                                    ) {
                                        if (chooseToBat) {
                                            CricketBatIcon(
                                                color = if (tossWon)
                                                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                            )
                                        } else {
                                            CricketBallIcon(
                                                color = if (tossWon)
                                                    MaterialTheme.colorScheme.onPrimaryContainer
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.scaledWidth(8.dp))
                                    
                                    // Clear message showing who won and what they chose
                                    val winner = gameViewModel.tossWinner
                                    val choiceText = if (chooseToBat) {
                                        "$winner chose to bat first"
                                    } else {
                                        "$winner chose to bowl first"
                                    }
                                    
                                    ScaledTitleMedium(
                                        text = choiceText,
                                        fontWeight = FontWeight.Medium,
                                        color = if (tossWon) 
                                            MaterialTheme.colorScheme.onPrimaryContainer 
                                        else 
                                            MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    
                    // Show batting/bowling choice when user wins the toss with improved animations
                    AnimatedVisibility(
                        visible = showBattingChoice && !battingChoiceMade && tossWon,
                        enter = fadeIn(animationSpec = tween(300, delayMillis = 300)) + 
                                expandVertically(animationSpec = tween(500)),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(vertical = 8.dp),
                            shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(uiScale.scaledDp(4.dp))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.scaledPadding(16.dp)
                            ) {
                                ScaledTitleMedium(
                                    text = "What would you like to do?",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.scaledPadding(vertical = 8.dp)
                                )
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .scaledPadding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(uiScale.scaledDp(16.dp))
                                ) {
                                    // Bat first button with icon
                                    CricketButton(
                                        onClick = {
                                            // Choose to bat first - the toss winner wants to bat
                                            gameViewModel.battingFirst = gameViewModel.tossWinner
                                            gameViewModel.bowlingFirst = if (gameViewModel.tossWinner == gameViewModel.team1Name) 
                                                gameViewModel.team2Name else gameViewModel.team1Name
                                            battingChoiceMade = true
                                        },
                                        buttonType = ButtonType.PRIMARY,
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(elevation = uiScale.scaledDp(4.dp), shape = RoundedCornerShape(uiScale.scaledDp(12.dp))),
                                        shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CricketBatIcon(
                                                modifier = Modifier.scaledSize(24.dp),
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                            
                                            Spacer(modifier = Modifier.scaledWidth(8.dp))
                                            
                                            CricketText(
                                                text = "Bat First",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    
                                    // Bowl first button with icon
                                    CricketButton(
                                        onClick = {
                                            // Choose to bowl first - the toss winner wants to bowl
                                            gameViewModel.bowlingFirst = gameViewModel.tossWinner
                                            gameViewModel.battingFirst = if (gameViewModel.tossWinner == gameViewModel.team1Name) 
                                                gameViewModel.team2Name else gameViewModel.team1Name
                                            battingChoiceMade = true
                                        },
                                        buttonType = ButtonType.SECONDARY,
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(elevation = uiScale.scaledDp(4.dp), shape = RoundedCornerShape(uiScale.scaledDp(12.dp))),
                                        shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CricketBallIcon(
                                                modifier = Modifier.scaledSize(20.dp),
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                            
                                            Spacer(modifier = Modifier.scaledWidth(8.dp))
                                            
                                            CricketText(
                                                text = "Bowl First",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // For Player B's choice in PVP mode when they win the toss
                    AnimatedVisibility(
                        visible = !tossWon && !battingChoiceMade && gameViewModel.gameMode.name == "PVP",
                        enter = fadeIn(animationSpec = tween(300, delayMillis = 300)) + 
                                expandVertically(animationSpec = tween(500)),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(vertical = 8.dp),
                            shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(uiScale.scaledDp(4.dp))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.scaledPadding(16.dp)
                            ) {
                                ScaledTitleMedium(
                                    text = "${gameViewModel.team2Name}'s choice",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.scaledPadding(vertical = 8.dp)
                                )
                                
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .scaledPadding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(uiScale.scaledDp(16.dp))
                                ) {
                                    // Bat first button for player B
                                    CricketButton(
                                        onClick = {
                                            // Player B chooses to bat first
                                            gameViewModel.battingFirst = gameViewModel.team2Name
                                            gameViewModel.bowlingFirst = gameViewModel.team1Name
                                            battingChoiceMade = true
                                        },
                                        buttonType = ButtonType.PRIMARY,
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(elevation = uiScale.scaledDp(4.dp), shape = RoundedCornerShape(uiScale.scaledDp(12.dp))),
                                        shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CricketBatIcon(
                                                modifier = Modifier.scaledSize(24.dp),
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                            
                                            Spacer(modifier = Modifier.scaledWidth(8.dp))
                                            
                                            CricketText(
                                                text = "Bat First",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    
                                    // Bowl first button for player B
                                    CricketButton(
                                        onClick = {
                                            // Player B chooses to bowl first
                                            gameViewModel.bowlingFirst = gameViewModel.team2Name
                                            gameViewModel.battingFirst = gameViewModel.team1Name
                                            battingChoiceMade = true
                                        },
                                        buttonType = ButtonType.SECONDARY,
                                        modifier = Modifier
                                            .weight(1f)
                                            .shadow(elevation = uiScale.scaledDp(4.dp), shape = RoundedCornerShape(uiScale.scaledDp(12.dp))),
                                        shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CricketBallIcon(
                                                modifier = Modifier.scaledSize(20.dp),
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                            
                                            Spacer(modifier = Modifier.scaledWidth(8.dp))
                                            
                                            CricketText(
                                                text = "Bowl First",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.scaledHeight(24.dp))
                    
                    // Show continue button only if the choice is made
                    AnimatedVisibility(
                        visible = battingChoiceMade,
                        enter = fadeIn(animationSpec = tween(300)) + 
                                expandVertically(animationSpec = tween(500)),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        CricketButton(
                            onClick = onTossComplete,
                            buttonType = ButtonType.PRIMARY,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .scaledHeight(56.dp),
                            shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
                        ) {
                            ScaledTitleMedium(
                                text = "Continue to Match",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        
        // Confetti effect when user wins the toss
        if (tossWon && showConfetti) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                for (i in 0 until confettiParticles.size) {
                    val particle = confettiParticles[i]
                    val progress = confettiAnimatable.value
                    
                    // Calculate particle position with gravity and wind effects
                    val x = particle.position.first + sin(progress * 0.02f + i) * 10f
                    val y = particle.position.second + progress * particle.speed
                    
                    // Only draw particles that are within the screen
                    if (y < size.height + 100) {
                        // Use drawIntoCanvas for managing canvas state properly
                        drawIntoCanvas { canvas ->
                            canvas.save()
                            // Apply rotation transformation
                            canvas.rotate(particle.rotation + progress * (if (i % 2 == 0) 1f else -1f), x + particle.size/2, y + particle.size/2)
                            // Draw the confetti particle
                            drawRect(
                                color = particle.color.copy(alpha = 1f - y / size.height),
                                topLeft = Offset(x, y),
                                size = Size(particle.size, particle.size)
                            )
                            canvas.restore()
                        }
                    }
                }
            }
        }
    }
}