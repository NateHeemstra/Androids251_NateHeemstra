package com.example.assignment3
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Needed when using `by` with state:
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudyTimerApp()
            }
        }
    }
}

@Composable
fun StudyTimerApp() {
    // State
    var isRunning by remember { mutableStateOf(false) }   // running flag
    var sessionLength by remember { mutableStateOf(25) }  // minutes
    var timeRemaining by remember { mutableStateOf(25 * 60) } // seconds
    var completedSessions by remember { mutableStateOf(0) }

    // Keep timeRemaining synced when stopped and user changes preset
    LaunchedEffect(sessionLength, isRunning) {
        if (!isRunning) timeRemaining = sessionLength * 60
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display
        TimerDisplay(timeRemaining = timeRemaining, sessionLength = sessionLength)
        Spacer(modifier = Modifier.height(32.dp))

        // Controls
        TimerControls(
            isRunning = isRunning,
            onToggleTimer = {
                if (!isRunning) {
                    if (timeRemaining <= 0) timeRemaining = sessionLength * 60
                    isRunning = true
                } else {
                    // Reset while running
                    isRunning = false
                    timeRemaining = sessionLength * 60
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Session settings
        SessionSettings(
            sessionLength = sessionLength,
            onSessionLengthChange = { sessionLength = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Completed sessions
        SessionStats(completedSessions = completedSessions)
    }

    // Countdown timer with LaunchedEffect
    LaunchedEffect(isRunning, timeRemaining) {
        if (isRunning && timeRemaining > 0) {
            delay(1000L)
            timeRemaining -= 1
        } else if (isRunning && timeRemaining == 0) {
            isRunning = false
            completedSessions += 1
        }
    }
}

@Composable
fun TimerDisplay(
    timeRemaining: Int,
    sessionLength: Int
) {
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val mmss = String.format("%02d:%02d", minutes, seconds)
    // progress if you want it later:
    // val progress = 1f - (timeRemaining.toFloat() / (sessionLength * 60f))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = mmss, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Session: ${sessionLength}m", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun TimerControls(
    isRunning: Boolean,
    onToggleTimer: () -> Unit
) {
    Button(onClick = onToggleTimer) {
        Text(if (isRunning) "Reset" else "Start")
    }
}

@Composable
fun SessionSettings(
    sessionLength: Int,
    onSessionLengthChange: (Int) -> Unit
) {
    val options = listOf(5, 15, 25, 45)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Session Length: ${sessionLength}", style = MaterialTheme.typography.titleMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { minutes ->
                val selected = minutes == sessionLength
                val width = Modifier.width(70.dp)

                if (selected) {
                    Button(onClick = { /* no-op */ }, modifier = width) {
                        Text("${minutes}")
                    }
                } else {
                    OutlinedButton(onClick = { onSessionLengthChange(minutes) }, modifier = width) {
                        Text("${minutes}")
                    }
                }
            }
        }
    }
}

@Composable
fun SessionStats(completedSessions: Int) {
    Text("Completed sessions: $completedSessions")
}

@Preview(showBackground = true)
@Composable
fun StudyTimerPreview() {
    MaterialTheme {
        StudyTimerApp()
    }
}