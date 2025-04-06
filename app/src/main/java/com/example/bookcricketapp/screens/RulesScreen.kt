package com.example.bookcricketapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(onNavigateBack: () -> Unit) {
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("How to Play") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Book Cricket Rules",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            RuleSection(
                title = "What is Book Cricket?",
                description = "Book Cricket is a simple indoor game that can be played using any book with numbered pages. It simulates a cricket match by using page numbers to determine runs scored."
            )
            
            RuleSection(
                title = "Game Setup",
                description = "1. Choose between single player (vs computer) or two players.\n" +
                              "2. Set team names, number of overs, and wickets per team.\n" +
                              "3. Perform a coin toss to determine which team bats first."
            )
            
            RuleSection(
                title = "How to Play",
                description = "1. The batting player taps the 'Flip Page' button which simulates opening a book to a random page.\n" +
                              "2. The last digit of the page number determines the runs:\n" +
                              "   - If it's 0: It's OUT! The batting team loses a wicket.\n" +
                              "   - If it's 1-9: The batting team scores that many runs.\n" +
                              "3. Players take turns batting until all overs are completed or all wickets are lost."
            )
            
            RuleSection(
                title = "Winning",
                description = "The team that scores more runs wins the match. If both teams score the same number of runs, the match is tied."
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "Back to Game",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun RuleSection(title: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            lineHeight = 24.sp
        )
    }
}