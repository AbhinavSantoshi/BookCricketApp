package com.example.bookcricketapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcricketapp.utils.*
import com.example.bookcricketapp.viewmodels.GameViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InningsBreakScreen(
    gameViewModel: GameViewModel,
    onNavigateToSecondInnings: () -> Unit
) {
    val battingFirst = gameViewModel.battingFirst
    val bowlingFirst = gameViewModel.bowlingFirst
    val uiScale = rememberUiScaleUtils()
    
    // Animation states
    var animationStarted by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        delay(100)
        animationStarted = true
    }
    
    // Background gradient
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        )
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    ScaledTitleMedium(
                        text = "Innings Break"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = backgroundGradient),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .scaledPadding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = animationStarted,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { -50 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    ScaledHeadlineSmall(
                        text = "End of First Innings",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scaledPadding(bottom = 24.dp)
                    )
                }
                
                // First innings summary card
                AnimatedVisibility(
                    visible = animationStarted,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { 100 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(16.dp)
                            .shadow(uiScale.scaledDp(8.dp), RoundedCornerShape(uiScale.scaledDp(16.dp))),
                        shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = uiScale.scaledDp(4.dp)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ScaledTitleLarge(
                                text = "$battingFirst",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.scaledHeight(16.dp))
                            
                            // Get the first innings score and wickets based on which team batted first
                            val firstInningsScore = if (battingFirst == gameViewModel.team1Name) {
                                gameViewModel.team1Score
                            } else {
                                gameViewModel.team2Score
                            }
                            
                            val firstInningsWickets = if (battingFirst == gameViewModel.team1Name) {
                                gameViewModel.team1Wickets
                            } else {
                                gameViewModel.team2Wickets
                            }
                            
                            val firstInningsBallsPlayed = if (battingFirst == gameViewModel.team1Name) {
                                gameViewModel.team1BallsPlayed
                            } else {
                                gameViewModel.team2BallsPlayed
                            }
                            
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(uiScale.scaledDp(4.dp))
                            ) {
                                ScaledText(
                                    text = "$firstInningsScore",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                ScaledText(
                                    text = "/$firstInningsWickets",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                    modifier = Modifier.scaledPadding(bottom = 8.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.scaledHeight(8.dp))
                            
                            ScaledTitleMedium(
                                text = "Overs: ${gameViewModel.getCurrentOver(firstInningsBallsPlayed)}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.scaledHeight(24.dp))
                            
                            // Target display with decorative line
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .scaledPadding(vertical = 8.dp)
                            ) {
                                Divider(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    thickness = uiScale.scaledDp(2.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center)
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .scaledPadding(horizontal = 16.dp)
                                ) {
                                    ScaledText(
                                        text = "TARGET",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.scaledHeight(16.dp))
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(uiScale.scaledDp(8.dp)))
                                    .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f))
                                    .scaledPadding(horizontal = 24.dp, vertical = 12.dp)
                            ) {
                                ScaledTitleMedium(
                                    text = "$bowlingFirst needs ${firstInningsScore + 1} runs to win",
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.scaledHeight(32.dp))
                
                // Ready text
                AnimatedVisibility(
                    visible = animationStarted,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { 50 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    ScaledBodyMedium(
                        text = "Ready for the second innings?",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Spacer(modifier = Modifier.scaledHeight(24.dp))
                
                // Continue button with animation
                AnimatedVisibility(
                    visible = animationStarted,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { 80 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Button(
                        onClick = {
                            // Prepare the game state for second innings
                            gameViewModel.prepareSecondInnings()
                            onNavigateToSecondInnings()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(horizontal = 32.dp)
                            .scaledHeight(56.dp),
                        shape = RoundedCornerShape(uiScale.scaledDp(28.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        ScaledTitleMedium(
                            text = "Start Second Innings"
                        )
                    }
                }
            }
        }
    }
}