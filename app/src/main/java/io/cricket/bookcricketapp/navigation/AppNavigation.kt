package io.cricket.bookcricketapp.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.cricket.bookcricketapp.components.ExitConfirmationDialog
import io.cricket.bookcricketapp.screens.*
import io.cricket.bookcricketapp.viewmodels.GameViewModel

// Define screen routes
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object About : Screen("about")
    object Settings : Screen("settings") // Add Settings route
    object MatchSettings : Screen("match_settings")
    object Toss : Screen("toss")
    object FirstInnings : Screen("first_innings")
    object InningsBreak : Screen("innings_break")
    object SecondInnings : Screen("second_innings")
    object Results : Screen("results")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    gameViewModel: GameViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNewGameClick = { navController.navigate(Screen.MatchSettings.route) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                gameViewModel = gameViewModel
            )
        }
        
        // About Screen
        composable(route = Screen.About.route) {
            RulesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Settings Screen
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                gameViewModel = gameViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Match Settings Screen
        composable(route = Screen.MatchSettings.route) {
            MatchSettingsScreen(
                gameViewModel = gameViewModel,
                onNavigateToToss = { navController.navigate(Screen.Toss.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Toss Screen
        composable(route = Screen.Toss.route) {
            // Handle back button on Toss screen
            var showExitDialog by remember { mutableStateOf(false) }
            
            BackHandler {
                showExitDialog = true
            }
            
            if (showExitDialog) {
                ExitConfirmationDialog(
                    onContinueClick = { showExitDialog = false },
                    onExitClick = {
                        showExitDialog = false
                        gameViewModel.resetGame()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onDismissRequest = { showExitDialog = false }
                )
            }
            
            TossScreen(
                gameViewModel = gameViewModel,
                onTossComplete = { navController.navigate(Screen.FirstInnings.route) }
            )
        }
        
        // First Innings Screen
        composable(route = Screen.FirstInnings.route) {
            // Handle back button on First Innings screen
            var showExitDialog by remember { mutableStateOf(false) }
            
            BackHandler {
                showExitDialog = true
            }
            
            if (showExitDialog) {
                ExitConfirmationDialog(
                    onContinueClick = { showExitDialog = false },
                    onExitClick = {
                        showExitDialog = false
                        gameViewModel.resetGame()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onDismissRequest = { showExitDialog = false }
                )
            }
            
            InningsScreen(
                gameViewModel = gameViewModel,
                isFirstInnings = true,
                onInningsComplete = { 
                    // Navigate to innings break screen between first and second innings
                    navController.navigate(Screen.InningsBreak.route)
                }
            )
        }
        
        // Innings Break Screen
        composable(route = Screen.InningsBreak.route) {
            // Handle back button on Innings Break screen
            var showExitDialog by remember { mutableStateOf(false) }
            
            BackHandler {
                showExitDialog = true
            }
            
            if (showExitDialog) {
                ExitConfirmationDialog(
                    onContinueClick = { showExitDialog = false },
                    onExitClick = {
                        showExitDialog = false
                        gameViewModel.resetGame()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onDismissRequest = { showExitDialog = false }
                )
            }
            
            InningsBreakScreen(
                gameViewModel = gameViewModel,
                onNavigateToSecondInnings = {
                    navController.navigate(Screen.SecondInnings.route) {
                        popUpTo(Screen.InningsBreak.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Second Innings Screen
        composable(route = Screen.SecondInnings.route) {
            // Handle back button on Second Innings screen
            var showExitDialog by remember { mutableStateOf(false) }
            
            BackHandler {
                showExitDialog = true
            }
            
            if (showExitDialog) {
                ExitConfirmationDialog(
                    onContinueClick = { showExitDialog = false },
                    onExitClick = {
                        showExitDialog = false
                        gameViewModel.resetGame()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onDismissRequest = { showExitDialog = false }
                )
            }
            
            // Ensure second innings stats are properly reset when this screen is entered
            LaunchedEffect(key1 = true) {
                if (!gameViewModel.isFirstInningsOver) {
                    gameViewModel.isFirstInningsOver = true
                }
                gameViewModel.prepareSecondInnings()
            }
            
            InningsScreen(
                gameViewModel = gameViewModel,
                isFirstInnings = false,
                onInningsComplete = { navController.navigate(Screen.Results.route) }
            )
        }
        
        // Results Screen
        composable(route = Screen.Results.route) {
            ResultsScreen(
                gameViewModel = gameViewModel,
                onPlayAgainClick = { 
                    // Navigate first, then reset game state to prevent 
                    // ResultsScreen from seeing the reset state (which briefly shows as a tie)
                    navController.navigate(Screen.MatchSettings.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                    // Only reset after navigation has been dispatched
                    gameViewModel.resetGame()
                },
                onHomeClick = {
                    // Navigate first, then reset game state to prevent 
                    // ResultsScreen from seeing the reset state (which briefly shows as a tie)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                    // Only reset after navigation has been dispatched
                    gameViewModel.resetGame()
                }
            )
        }
    }
}