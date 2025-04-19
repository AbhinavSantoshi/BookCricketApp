package io.cricket.bookcricketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import io.cricket.bookcricketapp.navigation.AppNavigation
import io.cricket.bookcricketapp.ui.theme.BookCricketAppTheme
import io.cricket.bookcricketapp.utils.ProvideUiScaleUtils
import io.cricket.bookcricketapp.viewmodels.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookCricketAppTheme {
                // Wrap the entire app content with our ProvideUiScaleUtils
                ProvideUiScaleUtils {
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
}