package com.example.bookcricketapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bookcricketapp.utils.*

@Composable
fun ExitConfirmationDialog(
    onContinueClick: () -> Unit,
    onExitClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val uiScale = rememberUiScaleUtils()
    
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scaledPadding(16.dp),
            shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = uiScale.scaledDp(8.dp)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .scaledPadding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ScaledHeadlineSmall(
                    text = "Exit Game?",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.scaledHeight(16.dp))
                
                ScaledBodyLarge(
                    text = "Do you want to exit the current game? Your progress will be lost.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.scaledHeight(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(uiScale.scaledDp(8.dp))
                ) {
                    // Continue game button
                    CricketButton(
                        onClick = onContinueClick,
                        buttonType = ButtonType.SECONDARY,
                        modifier = Modifier.weight(1f)
                    ) {
                        CricketText(
                            text = "Continue",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    // Exit button - give more weight to ensure full text fits
                    CricketButton(
                        onClick = onExitClick,
                        buttonType = ButtonType.PRIMARY,
                        modifier = Modifier.weight(1f)
                    ) {
                        CricketText(
                            text = "Exit Game",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}