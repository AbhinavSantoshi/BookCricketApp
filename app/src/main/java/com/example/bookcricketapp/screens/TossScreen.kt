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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcricketapp.R
import com.example.bookcricketapp.components.ButtonType
import com.example.bookcricketapp.components.CricketButton
import com.example.bookcricketapp.components.CricketText
import com.example.bookcricketapp.viewmodels.GameViewModel
import com.example.bookcricketapp.viewmodels.TossChoice
import com.example.bookcricketapp.viewmodels.TossResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun TossScreen(
    gameViewModel: GameViewModel,
    onTossComplete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    
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
    
    // Visual effects for coin flip
    val flipRotation = remember { Animatable(0f) }
    val coinElevation = remember { Animatable(4f) }
    val coinScale = remember { Animatable(1f) }
    
    // Background gradient
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surface
        )
    )
    
    // Function to perform the coin toss animation
    fun performToss() {
        coroutineScope.launch {
            showCoinOptions = false
            showInstructions = false
            tossInProgress = true
            
            // Initial coin animation (fly up)
            launch {
                coinScale.animateTo(
                    targetValue = 1.5f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
                coinElevation.animateTo(
                    targetValue = 24f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            
            // Flipping animation
            launch {
                // Fast flips
                flipRotation.animateTo(
                    targetValue = 1800f, // Multiple flips
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = LinearEasing
                    )
                )
                
                delay(100) // Short pause
                
                // Determine toss result
                val isHeads = Random.nextBoolean()
                coinResult = if (isHeads) TossResult.HEADS else TossResult.TAILS
                tossWon = (userSelectedHeads && isHeads) || (!userSelectedHeads && !isHeads)
                
                // Set toss winner based on result - correctly assign the team names
                if (tossWon) {
                    gameViewModel.tossWinner = gameViewModel.team1Name
                } else {
                    gameViewModel.tossWinner = gameViewModel.team2Name
                }
                
                // If computer wins, it chooses to bat
                if (!tossWon && gameViewModel.gameMode.name == "PVC") {
                    gameViewModel.battingFirst = gameViewModel.team2Name
                    gameViewModel.bowlingFirst = gameViewModel.team1Name
                } else if (!tossWon) {
                    // In PVP mode, default choice for simplicity
                    gameViewModel.battingFirst = gameViewModel.team2Name
                    gameViewModel.bowlingFirst = gameViewModel.team1Name
                }
                
                // Slow down and land
                coinElevation.animateTo(
                    targetValue = 4f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
                coinScale.animateTo(
                    targetValue = 1.2f,
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
                
                // Finish animation
                coinFlipAnimationFinished = true
                delay(500)
                showResult = true
                
                // If player won the toss, show batting/bowling choice
                if (tossWon) {
                    showBattingChoice = true
                }
            }
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            AnimatedVisibility(
                visible = !tossInProgress || showResult,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = if (showResult) "Toss Result" else "Coin Toss",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
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
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Choose heads or tails for the toss",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Winner of the toss will choose to bat or bowl",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Coin animation area
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                // The flipping coin - improved with proper circular shape
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .shadow(
                            elevation = coinElevation.value.dp,
                            shape = CircleShape,
                            clip = true
                        )
                        .graphicsLayer {
                            scaleX = coinScale.value
                            scaleY = coinScale.value
                            rotationY = flipRotation.value
                            cameraDistance = 12f * density  // Add perspective for better 3D effect
                        }
                        .clip(CircleShape) // Apply clip to ensure circular shape
                        .background(
                            if (flipRotation.value % 360 < 90 || flipRotation.value % 360 > 270)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.secondaryContainer
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Show different sides based on rotation
                    val isHeadsSideVisible = flipRotation.value % 360 < 90 || flipRotation.value % 360 > 270
                    
                    if (isHeadsSideVisible) {
                        // Heads side
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "H",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Tails side
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "T",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Coin choice buttons (Heads/Tails)
            AnimatedVisibility(
                visible = showCoinOptions,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
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
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        CricketText(
                            text = "Heads",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Tails button
                    CricketButton(
                        onClick = {
                            userSelectedHeads = false
                            performToss()
                        },
                        buttonType = ButtonType.SECONDARY,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
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
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (tossWon) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "It's ${coinResult?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: ""}!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (tossWon) 
                                    MaterialTheme.colorScheme.onPrimaryContainer 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "${gameViewModel.tossWinner} won the toss!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (tossWon) 
                                    MaterialTheme.colorScheme.onPrimaryContainer 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            
                            // Only show batting choice message when choice is made or AI chose
                            if (!tossWon || (tossWon && battingChoiceMade)) {
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = "${gameViewModel.battingFirst} will bat first",
                                    style = MaterialTheme.typography.titleSmall,
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
                    
                    // Show batting/bowling choice when user wins the toss
                    AnimatedVisibility(
                        visible = showBattingChoice && !battingChoiceMade,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "What would you like to do?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Bat first button
                                CricketButton(
                                    onClick = {
                                        // Choose to bat first
                                        gameViewModel.battingFirst = gameViewModel.team1Name
                                        gameViewModel.bowlingFirst = gameViewModel.team2Name
                                        battingChoiceMade = true
                                    },
                                    buttonType = ButtonType.PRIMARY,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    CricketText(
                                        text = "Bat First",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                // Bowl first button
                                CricketButton(
                                    onClick = {
                                        // Choose to bowl first
                                        gameViewModel.battingFirst = gameViewModel.team2Name
                                        gameViewModel.bowlingFirst = gameViewModel.team1Name
                                        battingChoiceMade = true
                                    },
                                    buttonType = ButtonType.SECONDARY,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    CricketText(
                                        text = "Bowl First",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Show continue button only if the choice is made or AI chose
                    AnimatedVisibility(
                        visible = !tossWon || (tossWon && battingChoiceMade),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        CricketButton(
                            onClick = onTossComplete,
                            buttonType = ButtonType.PRIMARY,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            CricketText(
                                text = "Continue to Match",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}