package com.example.bookcricketapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookcricketapp.screens.*
import com.example.bookcricketapp.viewmodels.GameViewModel

// Define screen routes
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object About : Screen("about")
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
                onAboutClick = { navController.navigate(Screen.About.route) }
            )
        }
        
        // About Screen
        composable(route = Screen.About.route) {
            RulesScreen(
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
            TossScreen(
                gameViewModel = gameViewModel,
                onTossComplete = { navController.navigate(Screen.FirstInnings.route) }
            )
        }
        
        // First Innings Screen
        composable(route = Screen.FirstInnings.route) {
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