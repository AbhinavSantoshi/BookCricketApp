package com.example.bookcricketapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcricketapp.R
import com.example.bookcricketapp.components.ButtonType
import com.example.bookcricketapp.components.CricketButton
import com.example.bookcricketapp.components.CricketText
import com.example.bookcricketapp.utils.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNewGameClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val uiScale = rememberUiScaleUtils()
    
    // Button press animations - keeping these for user feedback
    val newGameButtonScale = remember { Animatable(1f) }
    val howToPlayButtonScale = remember { Animatable(1f) }
    
    // Enhanced background gradient with more depth
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.surface
        ),
        startY = 0f,
        endY = 2000f
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        // Decorative shapes for background
        Box(
            modifier = Modifier
                .scaledSize(300.dp)
                .align(Alignment.TopCenter)
                .offset(y = uiScale.scaledDp((-50).dp))
                .alpha(0.08f)
                .background(
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scaledPadding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section with logo and title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .scaledPadding(top = 24.dp)
            ) {
                // App logo using the custom image
                Box(
                    modifier = Modifier
                        .scaledSize(160.dp)
                        .shadow(uiScale.scaledDp(12.dp), CircleShape)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.book_cricket_app_icon),
                        contentDescription = "Book Cricket App Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .scaledPadding(4.dp) // Add a small padding to create a border effect
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Spacer(modifier = Modifier.scaledHeight(24.dp))
                
                // Main title with enhanced styling and overflow handling but no shadow/border
                ScaledText(
                    text = "BOOK CRICKET",
                    fontSize = scaledSp(32f),
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    letterSpacing = scaledSp(1.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(horizontal = 8.dp)
                )
                
                Spacer(modifier = Modifier.scaledHeight(12.dp))
                
                // Tagline with improved styling and overflow handling
                ScaledText(
                    text = "The Classic Game, Now Digital!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom section with enhanced buttons
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scaledPadding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(
                        elevation = uiScale.scaledDp(16.dp),
                        shape = RoundedCornerShape(uiScale.scaledDp(24.dp)),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        ambientColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
                    ),
                shape = RoundedCornerShape(uiScale.scaledDp(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = uiScale.scaledDp(6.dp)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scaledPadding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(uiScale.scaledDp(16.dp))
                ) {
                    // New Game button with simple press animation
                    Box(
                        modifier = Modifier
                            .animateContentSize()
                            .graphicsLayer { 
                                scaleX = newGameButtonScale.value
                                scaleY = newGameButtonScale.value
                            }
                            .fillMaxWidth()
                    ) {
                        CricketButton(
                            onClick = {
                                coroutineScope.launch {
                                    newGameButtonScale.animateTo(
                                        targetValue = 0.95f,
                                        animationSpec = tween(100)
                                    )
                                    newGameButtonScale.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(100)
                                    )
                                    onNewGameClick()
                                }
                            },
                            buttonType = ButtonType.PRIMARY,
                            shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                            modifier = Modifier.scaledHeight(56.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(uiScale.scaledDp(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.scaledSize(24.dp)
                                )
                                
                                ScaledTitleMedium(
                                    text = "New Game",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    // How to Play button with simple press animation
                    Box(
                        modifier = Modifier
                            .animateContentSize()
                            .graphicsLayer { 
                                scaleX = howToPlayButtonScale.value
                                scaleY = howToPlayButtonScale.value
                            }
                            .fillMaxWidth()
                    ) {
                        CricketButton(
                            onClick = {
                                coroutineScope.launch {
                                    howToPlayButtonScale.animateTo(
                                        targetValue = 0.95f,
                                        animationSpec = tween(100)
                                    )
                                    howToPlayButtonScale.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(100)
                                    )
                                    onAboutClick()
                                }
                            },
                            buttonType = ButtonType.SECONDARY,
                            shape = RoundedCornerShape(uiScale.scaledDp(16.dp)),
                            modifier = Modifier.scaledHeight(56.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(uiScale.scaledDp(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.scaledSize(24.dp)
                                )
                                
                                ScaledTitleMedium(
                                    text = "How to Play",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.scaledHeight(12.dp))
            
            // Footer with simple styling
            Column(
                modifier = Modifier.scaledPadding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScaledText(
                    text = "v1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.scaledHeight(4.dp))
                
                ScaledText(
                    text = "© 2025 Santoshi Softwares",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }
    }
}