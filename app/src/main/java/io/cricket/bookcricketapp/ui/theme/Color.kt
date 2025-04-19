package io.cricket.bookcricketapp.ui.theme

import androidx.compose.ui.graphics.Color

// PRIMARY COLORS - Main brand colors
val CricketBlue = Color(0xFF2196F3)       // Main primary color - HEX: #2196F3, RGB: 33, 150, 243
val PitchGreen = Color(0xFF4CAF50)        // Secondary primary color - HEX: #4CAF50, RGB: 76, 175, 80

// SECONDARY COLORS - Complementary accent colors
val BoundaryOrange = Color(0xFFFF9800)    // Accent color - HEX: #FF9800, RGB: 255, 152, 0
val VictoryPurple = Color(0xFF9C27B0)     // Accent color - HEX: #9C27B0, RGB: 156, 39, 176
val ActionRed = Color(0xFFF44336)         // Accent color - HEX: #F44336, RGB: 244, 67, 54

// VARIANTS OF PRIMARY COLORS
val LightBlue = Color(0xFF64B5F6)         // Lighter variant of Cricket Blue
val DeepBlue = Color(0xFF1976D2)          // Darker variant for hover states
val DarkestBlue = Color(0xFF0D47A1)       // Darkest variant for active states

val LightGreen = Color(0xFF81C784)        // Lighter variant of Pitch Green
val DeepGreen = Color(0xFF388E3C)         // Darker variant for hover states
val DarkestGreen = Color(0xFF1B5E20)      // Darkest variant for active states

// NEUTRAL BASE COLORS - Light Theme
val LightSurface = Color(0xFFFFFBFF)      // Light theme surface - very light mint
val LightBackground = Color(0xFFF8F9F3)   // Light theme background
val LightOnSurface = Color(0xFF1A1C19)    // Light theme text color

// NEUTRAL BASE COLORS - Dark Theme
val DarkSurface = Color(0xFF1A1C19)       // Dark theme surface
val DarkBackground = Color(0xFF121212)    // Dark theme background
val DarkOnSurface = Color(0xFFE2E3DD)     // Dark theme text color

// GRAY SCALE - For UI elements
val LightGray = Color(0xFFE0E0E0)         // For cards and dividers
val MediumGray = Color(0xFFBDBDBD)        // For borders
val DarkGray = Color(0xFF757575)          // For secondary text
val VeryDarkGray = Color(0xFF424242)      // For subtle elements

// FUNCTIONAL COLORS
// We're re-using our main palette colors for these functional roles:
// Success = PitchGreen
// Warning = BoundaryOrange
// Error = ActionRed
// Info = CricketBlue

// Legacy colors kept for backward compatibility
val ScoreboardBlue = CricketBlue          // Now mapped to our main brand color
val VibrantBlue = DeepBlue                // Now mapped to our darker blue
val BrightGreen = LightGreen              // Now mapped to our lighter green
val VibrantGreen = PitchGreen             // Now mapped to our main green
val NightGreen = Color(0xFFA5D6A7)        // Kept for compatibility
val WicketBrown = Color(0xFFBCAAA4)       // Kept for compatibility
val DarkWood = Color(0xFFA1887F)          // Kept for compatibility
val LightWood = Color(0xFFD7CCC8)         // Kept for compatibility
val CricketYellow = BoundaryOrange        // Now mapped to our orange accent
val CricketOrange = BoundaryOrange        // Now mapped to our orange accent
val CricketRed = ActionRed                // Now mapped to our red accent
val CricketPurple = VictoryPurple         // Now mapped to our purple accent