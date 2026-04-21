package com.example.cps251assignment11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import com.example.cps251assignment11.ui.theme.CPS251Assignment11Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        val viewModel: NumbersViewModel by viewModels()
        setContent {
            CPS251Assignment11Theme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    SlidingNumberScreen(viewModel)
                }
            }
        }
    }}
/*
How does the drag gesture system determine when a tile should move?
Why represent the empty space as 0 instead of null or a separate empty state? How does this choice affect the rest of the code?
Why use LazyVerticalGrid instead of a regular Column with Row composables? What are the performance and code organization benefits?
  When a tile moves, which parts of the UI recompose? How does Compose know what to update when the puzzle state changes?
What prevents invalid moves (e.g., moving a tile that isn't adjacent to the empty space)? Where should this validation logic live?
 */