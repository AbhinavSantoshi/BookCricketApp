package io.cricket.bookcricketapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.cricket.bookcricketapp.utils.rememberUiScaleUtils
import io.cricket.bookcricketapp.utils.scaledHeight
import io.cricket.bookcricketapp.utils.scaledPadding
import io.cricket.bookcricketapp.utils.scaledSp
import io.cricket.bookcricketapp.utils.ScaledText
import io.cricket.bookcricketapp.utils.ScaledBodyMedium
import io.cricket.bookcricketapp.utils.ScaledTitleMedium
import io.cricket.bookcricketapp.viewmodels.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun GameResultScreen(
    gameViewModel: GameViewModel,
    onPlayAgain: () -> Unit,
    onBackToHome: () -> Unit
) {
    // Determine match result
    gameViewModel.determineWinner()
    
    val scrollState = rememberScrollState()
    val uiScale = rememberUiScaleUtils()
    
    // Animation states
    var showResultCard by remember { mutableStateOf(false) }
    var showInningsCards by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }
    
    // Gradient background colors
    val backgroundColor = when {
        gameViewModel.matchTied -> listOf(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.surface
        )
        gameViewModel.matchWinner == gameViewModel.team1Name -> listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            MaterialTheme.colorScheme.surface
        )
        else -> listOf(
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
            MaterialTheme.colorScheme.surface
        )
    }
    
    // Get the correct team names based on batting order
    val firstInningsTeamName = gameViewModel.battingFirst
    val secondInningsTeamName = gameViewModel.bowlingFirst
    
    // Get the correct scores based on batting order
    val firstInningsScore = if (firstInningsTeamName == gameViewModel.team1Name) 
                            gameViewModel.team1Score else gameViewModel.team2Score
    val firstInningsWickets = if (firstInningsTeamName == gameViewModel.team1Name) 
                             gameViewModel.team1Wickets else gameViewModel.team2Wickets
    val firstInningsBalls = if (firstInningsTeamName == gameViewModel.team1Name) 
                           gameViewModel.team1BallsPlayed else gameViewModel.team2BallsPlayed
    
    val secondInningsScore = if (secondInningsTeamName == gameViewModel.team1Name) 
                            gameViewModel.team1Score else gameViewModel.team2Score
    val secondInningsWickets = if (secondInningsTeamName == gameViewModel.team1Name) 
                              gameViewModel.team1Wickets else gameViewModel.team2Wickets
    val secondInningsBalls = if (secondInningsTeamName == gameViewModel.team1Name) 
                            gameViewModel.team1BallsPlayed else gameViewModel.team2BallsPlayed
    
    // Trigger animations sequentially
    LaunchedEffect(Unit) {
        delay(300)
        showResultCard = true
        delay(600)
        showInningsCards = true
        delay(400)
        showButtons = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(backgroundColor))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scaledPadding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.scaledHeight(32.dp))
            
            // Result Card
            AnimatedVisibility(
                visible = showResultCard,
                enter = fadeIn(animationSpec = tween(800)) + 
                      slideInVertically(animationSpec = tween(800)) { it / 2 }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(
                            elevation = uiScale.scaledDp(8.dp),
                            shape = RoundedCornerShape(uiScale.scaledDp(16.dp))
                        ),
                    shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            gameViewModel.matchTied -> MaterialTheme.colorScheme.surfaceVariant
                            gameViewModel.matchWinner == gameViewModel.team1Name -> 
                                MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ScaledText(
                            text = "MATCH RESULT",
                            fontSize = scaledSp(18f),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        
                        Spacer(modifier = Modifier.scaledHeight(16.dp))
                        
                        ScaledText(
                            text = when {
                                gameViewModel.matchTied -> "Match Tied!"
                                else -> "${gameViewModel.matchWinner} Wins!"
                            },
                            fontSize = scaledSp(28f),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.scaledHeight(16.dp))
                        
                        ScaledText(
                            text = gameViewModel.getMatchResultDescription(),
                            fontSize = scaledSp(16f),
                            textAlign = TextAlign.Center,
                            lineHeight = scaledSp(24f),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.scaledHeight(24.dp))
            
            // First Innings Summary
            AnimatedVisibility(
                visible = showInningsCards,
                enter = fadeIn(animationSpec = tween(800)) + 
                      slideInVertically(
                        animationSpec = tween(800, delayMillis = 300)
                      ) { it / 2 }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(uiScale.scaledDp(16.dp))),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(uiScale.scaledDp(4.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(20.dp)
                    ) {
                        ScaledTitleMedium(
                            text = "First Innings: $firstInningsTeamName",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.scaledPadding(bottom = 12.dp)
                        )
                        
                        Divider(
                            modifier = Modifier.scaledPadding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScaledBodyMedium(
                                text = "Score:",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            ScaledBodyMedium(
                                text = "$firstInningsScore/$firstInningsWickets",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScaledBodyMedium(
                                text = "Overs:",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            ScaledBodyMedium(
                                text = gameViewModel.getCurrentOver(firstInningsBalls),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScaledBodyMedium(
                                text = "Run Rate:",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            ScaledBodyMedium(
                                text = gameViewModel.getRunRate(firstInningsScore, firstInningsBalls),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.scaledHeight(16.dp))
            
            // Second Innings Summary
            AnimatedVisibility(
                visible = showInningsCards,
                enter = fadeIn(animationSpec = tween(800)) + 
                      slideInVertically(
                        animationSpec = tween(800, delayMillis = 600)
                      ) { it / 2 }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(uiScale.scaledDp(16.dp))),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(uiScale.scaledDp(4.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(20.dp)
                    ) {
                        ScaledTitleMedium(
                            text = "Second Innings: $secondInningsTeamName",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.scaledPadding(bottom = 12.dp)
                        )
                        
                        Divider(
                            modifier = Modifier.scaledPadding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScaledBodyMedium(
                                text = "Score:",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            ScaledBodyMedium(
                                text = "$secondInningsScore/$secondInningsWickets",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScaledBodyMedium(
                                text = "Overs:",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            ScaledBodyMedium(
                                text = gameViewModel.getCurrentOver(secondInningsBalls),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scaledPadding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScaledBodyMedium(
                                text = "Run Rate:",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                            ScaledBodyMedium(
                                text = gameViewModel.getRunRate(secondInningsScore, secondInningsBalls),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.scaledHeight(32.dp))
            
            // Buttons
            AnimatedVisibility(
                visible = showButtons,
                enter = fadeIn(animationSpec = tween(800)) + 
                      slideInVertically(
                        animationSpec = tween(800, delayMillis = 900)
                      ) { it / 2 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledHeight(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
                    ) {
                        ScaledTitleMedium(
                            text = "Play Again",
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.scaledHeight(16.dp))
                    
                    OutlinedButton(
                        onClick = onBackToHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledHeight(56.dp),
                        shape = RoundedCornerShape(uiScale.scaledDp(12.dp)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        ScaledTitleMedium(
                            text = "Back to Home",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.scaledHeight(32.dp))
        }
    }
}