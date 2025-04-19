package io.cricket.bookcricketapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import io.cricket.bookcricketapp.utils.*
import io.cricket.bookcricketapp.viewmodels.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    gameViewModel: GameViewModel,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val uiScale = rememberUiScaleUtils()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    ScaledTitleMedium(
                        text = "Settings",
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
                    ScaledTitleMedium(
                        text = "Game Settings",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .scaledPadding(bottom = 16.dp)
                            .semantics { heading() }
                    )
                    
                    // Haptic Feedback Setting
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .scaledPadding(end = 16.dp)
                        ) {
                            ScaledText(
                                text = "Haptic Feedback",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.scaledHeight(4.dp))
                            
                            ScaledText(
                                text = "Vibration when scoring boundaries or losing wickets",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }
                        
                        Switch(
                            checked = gameViewModel.isHapticFeedbackEnabled,
                            onCheckedChange = { gameViewModel.isHapticFeedbackEnabled = it },
                            colors = SwitchDefaults.colors(
                                // Enhanced contrast for checked state
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                // Enhanced contrast for unchecked state
                                uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                                uncheckedTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        )
                    }
                    
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    
                    // App version info
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scaledPadding(vertical = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        ScaledText(
                            text = "App Version",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.scaledHeight(4.dp))
                        
                        ScaledText(
                            text = "v1.0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Footer with back button matching app patterns
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .scaledPadding(horizontal = 32.dp, vertical = 16.dp)
                    .scaledHeight(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(uiScale.scaledDp(12.dp))
            ) {
                ScaledTitleSmall(
                    text = "Back to Home"
                )
            }
            
            // Footer copyright
            ScaledText(
                text = "© 2025 Santoshi Softwares",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.scaledPadding(vertical = 8.dp)
            )
        }
    }
}