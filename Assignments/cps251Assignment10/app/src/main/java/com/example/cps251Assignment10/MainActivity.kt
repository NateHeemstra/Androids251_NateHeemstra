package com.example.cps251Assignment10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.cps251Assignment10.ui.theme.RoomDatabaseDemoTheme

// MainActivity is the entry point of the Android application.
class MainActivity : ComponentActivity() {
    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the window to use light status bar icons for a modern look.
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Initialize the ViewModel using by viewModels.
        // The provideFactory method ensures the ViewModel has access to the application context.
        val viewModel: NoteViewModel by viewModels {
            NoteViewModel.provideFactory(application)
        }

        // Set up the Compose UI content for this activity.
        setContent {
            // Apply the custom theme for the application.
            RoomDatabaseDemoTheme {
                // Surface is a fundamental composable that applies background color and fills the screen.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Display the NoteScreen, passing the initialized ViewModel to it.
                    NoteScreen(viewModel)
                }
            }
        }
    }
}
/**
 * How does an app handle dark and light themes? What is the purpose of defining separate color schemes for each theme?
 * The app animates card colors and elevations when notes are marked as important. Explain the purpose of using animationSpec with tween() in these animations?
 * When the floating action button is clicked, the app requests focus on a text field. Explain why this focus management is important for user experience?
 * The app defines a custom shapes system with different corner radius values for different sizes (extraSmall, small, medium, large, extraLarge). Explain how this hierarchical shape system contributes to visual consistency in the Material Design system?
 * The app uses AnimatedVisibility to show and hide note content when cards are clicked. Explain the difference between using AnimatedVisibility versus simply using an if statement to conditionally show content. What user experience benefit does the animation provide?
 */