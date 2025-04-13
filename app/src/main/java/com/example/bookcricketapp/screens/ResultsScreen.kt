package com.example.bookcricketapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bookcricketapp.components.ButtonType
import com.example.bookcricketapp.components.CricketButton
import com.example.bookcricketapp.components.CricketText
import com.example.bookcricketapp.utils.*
import com.example.bookcricketapp.viewmodels.GameViewModel
import com.example.bookcricketapp.utils.standardSlideInBottom
import kotlinx.coroutines.delay

@Composable
fun ResultsScreen(
    gameViewModel: GameViewModel,
    onPlayAgainClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showContent by remember { mutableStateOf(false) }
    val uiScale = rememberUiScaleUtils()

    // Capture the game state when the screen first loads to prevent changes during navigation
    // This will preserve the match results even if the game state resets
    val initialState = remember {
        // Create a data class to hold all the relevant game state
        data class MatchResult(
            val team1Name: String,
            val team2Name: String,
            val team1Score: Int,
            val team1Wickets: Int,
            val team1BallsPlayed: Int,
            val team2Score: Int,
            val team2Wickets: Int,
            val team2BallsPlayed: Int,
            val battingFirst: String,
            val bowlingFirst: String,
            val totalOvers: Int,
            val wicketsPerTeam: Int
        )

        // Capture all the game state needed to display results
        MatchResult(
            team1Name = gameViewModel.team1Name,
            team2Name = gameViewModel.team2Name,
            team1Score = gameViewModel.team1Score,
            team1Wickets = gameViewModel.team1Wickets,
            team1BallsPlayed = gameViewModel.team1BallsPlayed,
            team2Score = gameViewModel.team2Score,
            team2Wickets = gameViewModel.team2Wickets,
            team2BallsPlayed = gameViewModel.team2BallsPlayed,
            battingFirst = gameViewModel.battingFirst,
            bowlingFirst = gameViewModel.bowlingFirst,
            totalOvers = gameViewModel.totalOvers,
            wicketsPerTeam = gameViewModel.wicketsPerTeam
        )
    }

    // Use the captured state values instead of directly accessing the ViewModel
    val team1Name = initialState.team1Name
    val team2Name = initialState.team2Name
    val team1Score = initialState.team1Score
    val team1Wickets = initialState.team1Wickets
    val team1Overs = "${initialState.team1BallsPlayed / 6}.${initialState.team1BallsPlayed % 6}"
    val team2Score = initialState.team2Score
    val team2Wickets = initialState.team2Wickets
    val team2Overs = "${initialState.team2BallsPlayed / 6}.${initialState.team2BallsPlayed % 6}"

    // Determine which team batted first and second
    val firstInningsTeamName = initialState.battingFirst
    val secondInningsTeamName = initialState.bowlingFirst

    // Set the correct scores based on batting order
    val firstInningsScore: Int
    val firstInningsWickets: Int
    val firstInningsOvers: String
    val secondInningsScore: Int
    val secondInningsWickets: Int
    val secondInningsOvers: String

    if (firstInningsTeamName == team1Name) {
        // Team 1 batted first
        firstInningsScore = team1Score
        firstInningsWickets = team1Wickets
        firstInningsOvers = team1Overs
        secondInningsScore = team2Score
        secondInningsWickets = team2Wickets
        secondInningsOvers = team2Overs
    } else {
        // Team 2 batted first
        firstInningsScore = team2Score
        firstInningsWickets = team2Wickets
        firstInningsOvers = team2Overs
        secondInningsScore = team1Score
        secondInningsWickets = team1Wickets
        secondInningsOvers = team1Overs
    }

    // Determine winner based on innings scores, not team numbers
    val isTie = firstInningsScore == secondInningsScore
    val firstInningsWon = firstInningsScore > secondInningsScore
    val winningTeam = if (isTie) "" else if (firstInningsWon) firstInningsTeamName else secondInningsTeamName
    val margin = if (isTie) {
        "Tie"
    } else if (firstInningsWon) {
        "${firstInningsScore - secondInningsScore} runs"
    } else {
        "${initialState.wicketsPerTeam - secondInningsWickets} wickets"
    }

    // Helper function to calculate run rate
    fun getRunRate(runs: Int, balls: Int): String {
        if (balls == 0) return "0.00"
        val runRate = (runs.toFloat() * 6) / balls
        return String.format("%.2f", runRate)
    }

    // Background gradient
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surface
        )
    )

    // Animate content appearance
    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = standardSlideInBottom(offsetY = 100)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .scaledPadding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                ScaledHeadlineMedium(
                    text = "MATCH RESULTS",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.scaledPadding(vertical = 16.dp)
                )

                // Winner announcement card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(vertical = 8.dp),
                    shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isTie)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = uiScale.scaledDp(8.dp)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ScaledHeadlineSmall(
                            text = if (isTie) "MATCH TIED!" else "$winningTeam WINS!",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isTie)
                                MaterialTheme.colorScheme.onTertiaryContainer
                            else
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        if (!isTie) {
                            Spacer(modifier = Modifier.scaledHeight(8.dp))
                            ScaledTitleMedium(
                                text = "by $margin",
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.scaledHeight(16.dp))

                // First innings summary
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(vertical = 8.dp),
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
                            .scaledPadding(16.dp)
                    ) {
                        ScaledLabelLarge(
                            text = "1ST INNINGS",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Divider(
                            modifier = Modifier.scaledPadding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ScaledTitleMedium(
                                text = firstInningsTeamName,
                                fontWeight = FontWeight.Bold
                            )

                            Row(verticalAlignment = Alignment.Bottom) {
                                ScaledText(
                                    text = "$firstInningsScore",
                                    fontSize = scaledSp(24f),
                                    fontWeight = FontWeight.Bold
                                )
                                ScaledText(
                                    text = "/$firstInningsWickets",
                                    fontSize = scaledSp(18f),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.scaledPadding(bottom = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.scaledHeight(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScaledBodyMedium(
                                text = "Overs: $firstInningsOvers",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )

                            ScaledBodyMedium(
                                text = "RR: ${getRunRate(firstInningsScore, if (firstInningsTeamName == team1Name) initialState.team1BallsPlayed else initialState.team2BallsPlayed)}",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // Second innings summary
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(vertical = 8.dp),
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
                            .scaledPadding(16.dp)
                    ) {
                        ScaledLabelLarge(
                            text = "2ND INNINGS",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Divider(
                            modifier = Modifier.scaledPadding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ScaledTitleMedium(
                                text = secondInningsTeamName,
                                fontWeight = FontWeight.Bold
                            )

                            Row(verticalAlignment = Alignment.Bottom) {
                                ScaledText(
                                    text = "$secondInningsScore",
                                    fontSize = scaledSp(24f),
                                    fontWeight = FontWeight.Bold
                                )
                                ScaledText(
                                    text = "/$secondInningsWickets",
                                    fontSize = scaledSp(18f),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.scaledPadding(bottom = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.scaledHeight(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScaledBodyMedium(
                                text = "Overs: $secondInningsOvers",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )

                            ScaledBodyMedium(
                                text = "RR: ${getRunRate(secondInningsScore, if (secondInningsTeamName == team1Name) initialState.team1BallsPlayed else initialState.team2BallsPlayed)}",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // Match summary
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(vertical = 8.dp),
                    shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ScaledBodyLarge(
                            text = if (isTie) {
                                "The match ended in a tie with both teams scoring $firstInningsScore runs in their allotted overs."
                            } else if (firstInningsWon) {
                                "$firstInningsTeamName successfully defended their total, winning by $margin."
                            } else {
                                val remainingBalls = initialState.totalOvers * 6 -
                                    (if (secondInningsTeamName == team1Name) initialState.team1BallsPlayed else initialState.team2BallsPlayed)
                                if (remainingBalls > 0) {
                                    "$secondInningsTeamName chased down the target with $remainingBalls balls remaining."
                                } else {
                                    "$secondInningsTeamName chased down the target on the last ball!"
                                }
                            },
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.scaledHeight(32.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(uiScale.scaledDp(16.dp))
                ) {
                    CricketButton(
                        onClick = onPlayAgainClick,
                        buttonType = ButtonType.PRIMARY,
                        modifier = Modifier.weight(1f)
                    ) {
                        CricketText(
                            text = "Play Again",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    CricketButton(
                        onClick = onHomeClick,
                        buttonType = ButtonType.SECONDARY,
                        modifier = Modifier.weight(1f)
                    ) {
                        CricketText(
                            text = "Home",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.scaledHeight(16.dp))
            }
        }
    }
}