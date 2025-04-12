package com.example.bookcricketapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcricketapp.viewmodels.GameMode
import com.example.bookcricketapp.viewmodels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InningsScreen(
    gameViewModel: GameViewModel,
    isFirstInnings: Boolean,
    onInningsComplete: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val battingTeam = if (isFirstInnings) gameViewModel.battingFirst else gameViewModel.bowlingFirst
    val bowlingTeam = if (isFirstInnings) gameViewModel.bowlingFirst else gameViewModel.battingFirst

    // Get the correct scores based on which team is batting, not assuming team1 bats first
    val currentScore = if (battingTeam == gameViewModel.team1Name) gameViewModel.team1Score else gameViewModel.team2Score
    val currentWickets = if (battingTeam == gameViewModel.team1Name) gameViewModel.team1Wickets else gameViewModel.team2Wickets
    val currentBalls = if (battingTeam == gameViewModel.team1Name) gameViewModel.team1BallsPlayed else gameViewModel.team2BallsPlayed

    var isAnimatingRun by remember { mutableStateOf(false) }
    var isWicketFalling by remember { mutableStateOf(false) }
    var flipMessageVisible by remember { mutableStateOf(false) }
    var flipMessage by remember { mutableStateOf("") }
    var currentRunAnimation by remember { mutableStateOf("") }
    var lastDigit by remember { mutableStateOf(0) }
    
    // Use a local state to track innings completion instead of checking on every recomposition
    var isInningsCompleted by remember { mutableStateOf(false) }
    
    // Track when computer innings is complete but waiting for user to continue
    var isComputerInningsComplete by remember { mutableStateOf(false) }

    // Only check if innings is complete when something changes that could affect it
    LaunchedEffect(key1 = currentScore, key2 = currentWickets, key3 = currentBalls) {
        // Don't check for completion at the start of a new innings
        // Only check after at least one ball has been played or if computer innings is complete
        if ((currentBalls > 0 && !isFirstInnings) || (currentBalls > 0 && isFirstInnings) || isComputerInningsComplete) {
            isInningsCompleted = gameViewModel.isInningsComplete(isFirstInnings)
        }
    }
    
    // Only have the computer bat if:
    // 1. It's PVC mode AND
    // 2. Either it's the first innings and computer is team2Name (batting first)
    //    OR it's the second innings and computer is team2Name (batting second)
    val isComputerBatting = gameViewModel.gameMode == GameMode.PVC && battingTeam == gameViewModel.team2Name

    // Update to use the batting team for score tracking instead of assuming innings order
    val finalScore = remember { derivedStateOf { 
        if (battingTeam == gameViewModel.team1Name) gameViewModel.team1Score else gameViewModel.team2Score 
    } }
    val finalWickets = remember { derivedStateOf { 
        if (battingTeam == gameViewModel.team1Name) gameViewModel.team1Wickets else gameViewModel.team2Wickets 
    } }
    val finalBalls = remember { derivedStateOf { 
        if (battingTeam == gameViewModel.team1Name) gameViewModel.team1BallsPlayed else gameViewModel.team2BallsPlayed 
    } }

    LaunchedEffect(key1 = isComputerBatting) {
        if (isComputerBatting && !isInningsCompleted) {
            delay(1000)
            gameViewModel.computerPlay()
            delay(500)
            
            // Only after computer's innings, check if it was the first innings
            if (isFirstInnings) {
                // Mark first innings as complete in the ViewModel
                gameViewModel.isFirstInningsOver = true
                isComputerInningsComplete = true
                // Add delay then navigate to innings break
                delay(1500)
                onInningsComplete()
            } else {
                // If it's second innings, check game over then navigate to results
                gameViewModel.checkGameOver()
                isComputerInningsComplete = true
                // Add delay then navigate to results
                delay(1500)
                onInningsComplete()
            }
        }
    }

    if (isComputerBatting && !isInningsCompleted && !isComputerInningsComplete) {
        ComputerBattingLoadingScreen(battingTeam)
        return
    }

    fun handlePageFlip() {
        if (isAnimatingRun || isWicketFalling) {
            return
        }

        val runs = gameViewModel.playBall(isFirstInnings)
        val runType = gameViewModel.currentRunType
        val pageNumber = gameViewModel.lastPageNumber
        lastDigit = pageNumber % 10

        flipMessageVisible = true
        flipMessage = when (runType) {
            GameViewModel.RunType.OUT -> "OUT! Wicket falls on page $pageNumber"
            GameViewModel.RunType.BOUNDARY -> "$runs Runs! Boundary scored on page $pageNumber"
            GameViewModel.RunType.NORMAL_RUN -> "$runs Runs scored on page $pageNumber"
            GameViewModel.RunType.NO_RUN -> "No run on page $pageNumber"
        }

        when (runType) {
            GameViewModel.RunType.OUT -> {
                isWicketFalling = true
                scope.launch {
                    delay(2000)
                    isWicketFalling = false
                }
            }
            GameViewModel.RunType.BOUNDARY -> {
                isAnimatingRun = true
                currentRunAnimation = if (runs == 4) "four" else "six"
                scope.launch {
                    delay(1500)
                    isAnimatingRun = false
                }
            }
            else -> { /* No special animation */ }
        }

        // Check if innings is complete after this ball
        val maxBalls = gameViewModel.totalOvers * 6
        val currentBalls = if (battingTeam == gameViewModel.team1Name) gameViewModel.team1BallsPlayed else gameViewModel.team2BallsPlayed
        val currentWickets = if (battingTeam == gameViewModel.team1Name) gameViewModel.team1Wickets else gameViewModel.team2Wickets

        // Check if all balls played or all wickets lost (standard innings completion)
        if (gameViewModel.isInningsComplete(isFirstInnings) || 
            (isFirstInnings && (currentBalls >= maxBalls || currentWickets >= gameViewModel.wicketsPerTeam))) {
            
            if (isFirstInnings) {
                gameViewModel.isFirstInningsOver = true
                // Show ball result animation briefly, then navigate to innings break
                scope.launch {
                    delay(1500)
                    onInningsComplete()
                }
            } else {
                // For second innings completion, set game over and navigate
                gameViewModel.checkGameOver()
                scope.launch {
                    delay(1500)
                    onInningsComplete()
                }
            }
        }
    }

    val gradientColors = if (isFirstInnings) {
        listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.surface
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.surface
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = gradientColors))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFirstInnings)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${if (isFirstInnings) "1st" else "2nd"} INNINGS",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isFirstInnings)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "BATTING",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = battingTeam,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "BOWLING",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = bowlingTeam,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$currentScore",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = " / ",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$currentWickets",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "OVERS",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${currentBalls / 6}.${currentBalls % 6} / ${gameViewModel.totalOvers}.0",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val progress = currentBalls.toFloat() / (gameViewModel.totalOvers * 6)
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (isFirstInnings)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard(
                            title = "RUN RATE",
                            value = gameViewModel.getRunRate(currentScore, currentBalls),
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (isFirstInnings) {
                            StatCard(
                                title = "PROJ. SCORE",
                                value = gameViewModel.getProjectedScore(currentScore, currentBalls).toString(),
                                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            // Determine target based on which team batted first
                            val firstInningsScore = if (gameViewModel.battingFirst == gameViewModel.team1Name) {
                                gameViewModel.team1Score
                            } else {
                                gameViewModel.team2Score
                            }
                            
                            StatCard(
                                title = "TARGET",
                                value = "${firstInningsScore + 1}",
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (!isFirstInnings) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Determine first innings score based on batting order
                        val firstInningsScore = if (gameViewModel.battingFirst == gameViewModel.team1Name) {
                            gameViewModel.team1Score
                        } else {
                            gameViewModel.team2Score
                        }
                        
                        val remainingRuns = firstInningsScore + 1 - currentScore
                        val remainingBalls = gameViewModel.totalOvers * 6 - currentBalls

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (remainingRuns <= remainingBalls / 2)
                                    MaterialTheme.colorScheme.primaryContainer
                                else if (remainingRuns <= remainingBalls)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else
                                    MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "NEEDED",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "$remainingRuns runs from $remainingBalls balls",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "Required rate: ${String.format("%.2f", remainingRuns.toFloat() * 6 / remainingBalls.coerceAtLeast(1))}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }

            // Flexible space that shrinks on smaller screens
            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = flipMessageVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            flipMessage.contains("OUT") -> MaterialTheme.colorScheme.errorContainer
                            flipMessage.contains("Boundary") -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val baseColor = when {
                            flipMessage.contains("OUT") -> MaterialTheme.colorScheme.onErrorContainer
                            flipMessage.contains("Boundary") -> MaterialTheme.colorScheme.onPrimaryContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        
                        // Extract run value from the flipMessage for display
                        val runValue = when {
                            flipMessage.contains("OUT") -> "W"
                            flipMessage.contains("No run") -> "0"
                            else -> {
                                val words = flipMessage.split(" ")
                                words.firstOrNull { it.toIntOrNull() != null } ?: "0"
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color = baseColor.copy(alpha = 0.2f),
                                    shape = CircleShape
                                )
                                .border(
                                    width = 2.dp,
                                    color = baseColor.copy(alpha = 0.5f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = runValue,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = baseColor
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = flipMessage,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = baseColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (isWicketFalling || isAnimatingRun) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp) // Reduced height
                        .padding(vertical = 4.dp), // Reduced padding
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isWicketFalling -> MaterialTheme.colorScheme.error
                                currentRunAnimation == "four" -> MaterialTheme.colorScheme.tertiary // Changed from primary to tertiary (BoundaryOrange)
                                else -> MaterialTheme.colorScheme.tertiary
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 16.dp), // Reduced padding
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val animationText = when {
                                isWicketFalling -> "WICKET!"
                                currentRunAnimation == "four" -> "FOUR!"
                                currentRunAnimation == "six" -> "SIX!"
                                else -> ""
                            }

                            Text(
                                text = animationText,
                                style = MaterialTheme.typography.headlineMedium, // Reduced text size
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { if (!isInningsCompleted) handlePageFlip() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), // Reduced button height
                enabled = !isInningsCompleted && 
                        (!gameViewModel.isComputerPlaying) &&
                        (!isWicketFalling) && 
                        (!isAnimatingRun) && 
                        !isComputerInningsComplete &&
                        // Check if we haven't reached the maximum overs
                        currentBalls < (gameViewModel.totalOvers * 6) &&
                        // Check if we haven't lost all wickets
                        currentWickets < gameViewModel.wicketsPerTeam,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInningsCompleted)
                        MaterialTheme.colorScheme.surfaceVariant
                    else
                        MaterialTheme.colorScheme.primary,
                    contentColor = if (isInningsCompleted)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    text = if (isInningsCompleted) "Innings Complete" else "Flip Page",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Added small padding at the bottom to ensure the button isn't flush with screen edge
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ComputerBattingLoadingScreen(teamName: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        ),
        label = "rotate"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$teamName's Innings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 8.dp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Computer is batting...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Please wait",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}