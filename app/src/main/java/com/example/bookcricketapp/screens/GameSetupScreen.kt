package com.example.bookcricketapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookcricketapp.components.ToggleGroup
import com.example.bookcricketapp.viewmodels.GameMode
import com.example.bookcricketapp.viewmodels.GameViewModel

@Composable
fun GameSetupScreen(
    gameViewModel: GameViewModel,
    onNavigateToToss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Game Mode Selection
        val gameModeTitles = listOf("Vs Computer", "Players vs Players")
        val gameModeValues = listOf(GameMode.PVC, GameMode.PVP)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Game Mode:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.width(110.dp)
            )

            ToggleGroup(
                options = gameModeTitles,
                selectedOption = if (gameViewModel.gameMode == GameMode.PVC)
                    gameModeTitles[0] else gameModeTitles[1],
                onOptionSelected = { title ->
                    val index = gameModeTitles.indexOf(title)
                    if (index != -1) {
                        gameViewModel.updateGameMode(gameModeValues[index])
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Team Names Input Fields
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Team 1 name input
            OutlinedTextField(
                value = gameViewModel.team1Name,
                onValueChange = { gameViewModel.team1Name = it },
                label = { Text("Team 1 Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Team 2 name input
            OutlinedTextField(
                value = gameViewModel.team2Name,
                onValueChange = { gameViewModel.team2Name = it },
                label = { Text("Team 2 Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = gameViewModel.gameMode != GameMode.PVC // Disable when in PVC mode
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToToss,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Start Game")
        }
    }
}