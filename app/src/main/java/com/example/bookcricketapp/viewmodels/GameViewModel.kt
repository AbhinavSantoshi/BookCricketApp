package com.example.bookcricketapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt
import kotlin.random.Random

enum class GameMode {
    SINGLE_PLAYER,
    TWO_PLAYER,
    PVP,  // Player vs Player
    PVC   // Player vs Computer
}

enum class TossChoice {
    HEADS,
    TAILS
}

enum class TossResult {
    HEADS,
    TAILS
}

enum class BattingChoice {
    BAT,
    BOWL
}

class GameViewModel : ViewModel() {

    // Enum to represent different types of runs
    enum class RunType {
        OUT,        // Wicket falls (0)
        NORMAL_RUN, // Normal runs (1, 2, 3)
        BOUNDARY,   // Boundary (4, 6)
        NO_RUN      // No run (5, 7, 8, 9)
    }

    // Game setup properties
    var gameMode by mutableStateOf(GameMode.PVC)
    var team1Name by mutableStateOf("Player")
    var team2Name by mutableStateOf("Computer")
    var tossWinner by mutableStateOf("")
    var battingFirst by mutableStateOf("")
    var bowlingFirst by mutableStateOf("")
    var currentBattingTeam by mutableStateOf("")
    var currentBowlingTeam by mutableStateOf("")

    // Game Configuration
    var totalOvers by mutableStateOf(2)
    var wicketsPerTeam by mutableStateOf(10)
    var totalWickets by mutableStateOf(10) // Added for compatibility with InningsScreen reference

    // First Innings
    var team1Score by mutableStateOf(0)
    var team1Wickets by mutableStateOf(0)
    var team1BallsPlayed by mutableStateOf(0)
    
    // Second Innings
    var team2Score by mutableStateOf(0)
    var team2Wickets by mutableStateOf(0)
    var team2BallsPlayed by mutableStateOf(0)
    
    // Current game state
    var currentRun by mutableStateOf(0)
    var lastPageNumber by mutableStateOf(0)
    var currentRunType by mutableStateOf(RunType.NO_RUN)
    var isGameOver by mutableStateOf(false)
    var winningTeam by mutableStateOf("")
    var winningMargin by mutableStateOf("")
    var matchTied by mutableStateOf(false)
    var matchWinner by mutableStateOf("")
    var isFirstInningsOver by mutableStateOf(false)

    // Computer play related states
    var isComputerPlaying by mutableStateOf(false)
    var isComputerInningsComplete by mutableStateOf(false)
    var computerBallDelay by mutableStateOf(1000L) // Milliseconds between computer plays

    fun updateMatchSettings(team1: String, team2: String, overs: Int, wickets: Int) {
        team1Name = team1
        team2Name = team2
        totalOvers = overs
        wicketsPerTeam = wickets
    }
    
    // Renamed from setGameMode to updateGameMode to avoid JVM signature clash with the gameMode property setter
    fun updateGameMode(mode: GameMode) {
        gameMode = mode
        // Update team names based on selected game mode
        if (mode == GameMode.PVC) {
            team1Name = "Player A"
            team2Name = "Computer"
        } else {
            team1Name = "Player 1"
            team2Name = "Player 2"
        }
    }

    fun performToss(choice: TossChoice, selectedByTeam1: Boolean): Boolean {
        val tossResult = TossChoice.values()[Random.nextInt(0, 2)]
        val team1Won = tossResult == choice
        
        tossWinner = if ((team1Won && selectedByTeam1) || (!team1Won && !selectedByTeam1)) {
            team1Name
        } else {
            team2Name
        }
        
        return team1Won && selectedByTeam1 || !team1Won && !selectedByTeam1
    }
    
    fun chooseBattingOrder(battingFirstChoice: Boolean) {
        battingFirst = if (battingFirstChoice) tossWinner else {
            if (tossWinner == team1Name) team2Name else team1Name
        }
        
        bowlingFirst = if (battingFirst == team1Name) team2Name else team1Name
    }
    
    fun playBall(isFirstInnings: Boolean): Int {
        // Generate a random page number between 1 and 500
        lastPageNumber = Random.nextInt(1, 500)
        
        // Get the last digit of the page number
        val lastDigit = lastPageNumber % 10
        
        // Determine run type and runs based on the last digit
        val runType = when(lastDigit) {
            0 -> RunType.OUT           // Wicket falls
            1, 2, 3 -> RunType.NORMAL_RUN  // Normal runs
            4, 6 -> RunType.BOUNDARY    // Boundary runs
            else -> RunType.NO_RUN      // No run (5, 7, 8, 9)
        }
        
        // Calculate runs based on run type
        val runs = when(runType) {
            RunType.OUT -> 0
            RunType.NORMAL_RUN -> lastDigit  // 1, 2, or 3 runs
            RunType.BOUNDARY -> lastDigit    // 4 or 6 runs
            RunType.NO_RUN -> 0              // No runs scored
        }
        
        // Store the current run value and run type
        currentRun = runs
        currentRunType = runType
        
        // Update the game state based on which innings is being played
        if (isFirstInnings) {
            if (runType == RunType.OUT) {
                team1Wickets++
            } else if (runs > 0) {
                team1Score += runs
            }
            team1BallsPlayed++
        } else {
            if (runType == RunType.OUT) {
                team2Wickets++
            } else if (runs > 0) {
                team2Score += runs
            }
            team2BallsPlayed++
        }
        
        return runs
    }
    
    fun isInningsComplete(isFirstInnings: Boolean): Boolean {
        val ballsPlayed = if (isFirstInnings) team1BallsPlayed else team2BallsPlayed
        val wickets = if (isFirstInnings) team1Wickets else team2Wickets
        val maxBalls = totalOvers * 6
        
        // Check if all balls are played or all wickets are lost
        if (ballsPlayed >= maxBalls || wickets >= wicketsPerTeam) {
            return true
        }
        
        // For second innings, also check if the batting team has surpassed the target
        if (!isFirstInnings && team2Score > team1Score) {
            determineWinner()
            return true
        }
        
        return false
    }
    
    fun computerPlay() {
        // For computer player, simulate entire innings quickly
        val maxBalls = totalOvers * 6
        
        while (team2BallsPlayed < maxBalls && team2Wickets < wicketsPerTeam && team2Score <= team1Score) {
            playBall(false)
        }
    }
    
    fun determineWinner() {
        isGameOver = true
        matchTied = false
        
        if (team1Score > team2Score) {
            winningTeam = team1Name
            winningMargin = "${team1Score - team2Score} runs"
            matchWinner = team1Name
        } else if (team2Score > team1Score) {
            winningTeam = team2Name
            winningMargin = "${wicketsPerTeam - team2Wickets} wickets"
            matchWinner = team2Name
        } else {
            winningTeam = "None"
            winningMargin = "Match tied"
            matchTied = true
            matchWinner = ""
        }
    }
    
    fun checkGameOver() {
        if (isFirstInningsOver && isInningsComplete(false)) {
            isGameOver = true
            determineWinner()
        }
    }
    
    fun getRunRate(runs: Int, balls: Int): String {
        if (balls == 0) return "0.00"
        val runRate = (runs.toFloat() * 6) / balls
        return String.format("%.2f", runRate)
    }
    
    fun getProjectedScore(runs: Int, balls: Int): Int {
        if (balls == 0) return 0
        val totalBalls = totalOvers * 6
        return (runs.toDouble() / balls * totalBalls).toInt()
    }
    
    fun getMatchResultDescription(): String {
        return when {
            matchTied -> "Both teams scored ${team1Score} runs in an exciting match that ended in a tie!"
            matchWinner == team1Name -> "${team1Name} won by $winningMargin by successfully defending their total of ${team1Score} runs."
            matchWinner == team2Name -> {
                val remainingBalls = (totalOvers * 6) - team2BallsPlayed
                if (remainingBalls > 0) {
                    "${team2Name} won by $winningMargin with $remainingBalls balls remaining."
                } else {
                    "${team2Name} won by $winningMargin on the last ball!"
                }
            }
            else -> "Match completed."
        }
    }
    
    fun resetGame() {
        // Reset game state for a new match
        team1Score = 0
        team1Wickets = 0
        team1BallsPlayed = 0
        team2Score = 0
        team2Wickets = 0
        team2BallsPlayed = 0
        currentRun = 0
        lastPageNumber = 0
        currentRunType = RunType.NO_RUN
        isGameOver = false
        winningTeam = ""
        winningMargin = ""
        matchTied = false
        matchWinner = ""
        isFirstInningsOver = false
    }
    
    fun getCurrentOver(ballsPlayed: Int): String {
        val overs = ballsPlayed / 6
        val balls = ballsPlayed % 6
        return "$overs.${balls}"
    }

    fun isComputerInnings(): Boolean {
        // If it's second innings and computer is batting second OR
        // if it's first innings and computer is batting first
        return (isFirstInningsOver && bowlingFirst == team1Name) ||
               (!isFirstInningsOver && battingFirst == team2Name && gameMode == GameMode.PVC)
    }
    
    // Function to play a single ball for computer
    fun playComputerBall() {
        val isFirstInn = !isFirstInningsOver
        playBall(isFirstInn)
        
        // Check if the innings is over after this ball
        if (isInningsComplete(isFirstInn)) {
            isComputerPlaying = false
            isComputerInningsComplete = true
        }
    }
    
    // Function to start computer's entire innings
    fun startComputerInnings() {
        isComputerPlaying = true
        isComputerInningsComplete = false
    }
    
    // Function to stop computer innings
    fun stopComputerInnings() {
        isComputerPlaying = false
    }
    
    override fun onCleared() {
        super.onCleared()
        stopComputerInnings()
    }

    // Smart AI for computer player's bowling
    fun getSmartPageNumber(currentScore: Int, wickets: Int, overs: Int, target: Int): Int {
        // Base random range for page number
        val basePageNumber = (1..300).random()
        
        // If this is the second innings, adapt bowling based on match situation
        if (target > 0) {
            val remainingRuns = target - currentScore
            val remainingOvers = totalOvers - overs
            val requiredRunRate = if (remainingOvers > 0) remainingRuns.toFloat() / remainingOvers else 100f
            
            // Adjust bowling difficulty based on required run rate
            if (requiredRunRate > 12) {
                // Batsman needs high run rate, increase chance of wickets
                // Return more pages that end with 0 or 7 (out)
                val specialPages = listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 
                                          7, 17, 27, 37, 47, 57, 67, 77, 87, 97, 107, 117, 127, 137)
                return if (Random.nextFloat() < 0.4f) specialPages.random() else basePageNumber
            } else if (requiredRunRate < 5 && wickets < 7) {
                // Easy chase, try to take wickets
                val outPages = listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 170, 270)
                return if (Random.nextFloat() < 0.35f) outPages.random() else basePageNumber
            }
        } 
        // First innings or normal situation
        else {
            // In early overs, be more attacking
            if (overs < 3) {
                val wicketPages = listOf(10, 20, 30, 40, 70, 170, 270)
                return if (Random.nextFloat() < 0.3f) wicketPages.random() else basePageNumber
            }
            // In later overs, adjust based on score
            else if (overs >= totalOvers - 2) {
                // Try to prevent boundaries in death overs
                val nonBoundaryPages = (1..300).filter { it % 10 != 8 && it % 10 != 9 }
                return if (Random.nextFloat() < 0.4f) nonBoundaryPages.random() else basePageNumber
            }
        }
        
        // Default - just return a random page
        return basePageNumber
    }
}