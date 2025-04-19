package io.cricket.bookcricketapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.cricket.bookcricketapp.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(onNavigateBack: () -> Unit) {
    val scrollState = rememberScrollState()
    val uiScale = rememberUiScaleUtils()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    ScaledTitleMedium(
                        text = "How to Play",
                        modifier = Modifier.semantics { heading() }
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.scaledSize(48.dp)
                    ) {
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
                .scaledPadding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScaledHeadlineSmall(
                text = "Book Cricket Rules",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .scaledPadding(bottom = 24.dp)
                    .semantics { heading() }
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
                              "2. The last digit of the page number determines the outcome:\n" +
                              "   - If it's 0: It's OUT! The batting team loses a wicket.\n" +
                              "   - If it's 1, 2, or 3: The batting team scores that many runs.\n" +
                              "   - If it's 4 or 6: The batting team scores a boundary (4 or 6 runs).\n" +
                              "   - If it's 5, 7, 8, or 9: No run is scored.\n" +
                              "3. The first innings continues until all overs are completed or all wickets are lost.\n" +
                              "4. In the second innings, the team chases the target set by the first team.\n" +
                              "5. The second innings ends when the target is reached, all overs are completed, or all wickets are lost."
            )
            
            RuleSection(
                title = "Winning",
                description = "The team that scores more runs wins the match. If both teams score the same number of runs, the match is tied."
            )
            
            Spacer(modifier = Modifier.scaledHeight(24.dp))
            
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .scaledPadding(horizontal = 32.dp)
                    .scaledHeight(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                ScaledTitleSmall(
                    text = "Back to Game"
                )
            }
        }
    }
}

@Composable
fun RuleSection(title: String, description: String) {
    val uiScale = rememberUiScaleUtils()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scaledPadding(bottom = 24.dp)
    ) {
        ScaledTitleMedium(
            text = title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .scaledPadding(bottom = 8.dp)
                .semantics { heading() }
        )
        
        ScaledBodyMedium(
            text = description,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = scaledSp(24f)
        )
    }
}