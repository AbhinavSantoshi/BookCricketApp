package com.example.bookcricketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookcricketapp.navigation.AppNavigation
import com.example.bookcricketapp.ui.theme.BookCricketAppTheme
import com.example.bookcricketapp.viewmodels.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookCricketAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create the navigation controller
                    val navController = rememberNavController()
                    
                    // Create the game view model
                    val gameViewModel: GameViewModel = viewModel()
                    
                    // Set up navigation with both required parameters
                    AppNavigation(
                        navController = navController,
                        gameViewModel = gameViewModel
                    )
                }
            }
        }
    }
}