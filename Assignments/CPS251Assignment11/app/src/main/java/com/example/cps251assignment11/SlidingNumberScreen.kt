package com.example.cps251assignment11

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlidingNumberScreen(viewModel: NumbersViewModel) {

    val tiles = viewModel.tiles.value
    val moves = viewModel.moves.value
    val isSolved = viewModel.isSolved()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sliding Puzzle") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (isSolved) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Puzzle Solved!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                "Moves: $moves",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Card(
                        modifier = Modifier.width(120.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Moves",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "$moves",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }


                    Button(
                        onClick = { viewModel.resetGame() },
                        modifier = Modifier.fillMaxWidth(0.6f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Reset", style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {viewModel.solvePuzze()},
                        modifier = Modifier.fillMaxWidth(0.6f),
                        shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {Text ("Solve") }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .size(320.dp)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    itemsIndexed(tiles) { index, tile ->

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .background(
                                    color = if (tile == 0) Color.Transparent else Color(0xFFE3F2FD),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .pointerInput(index) {
                                    var totalDragX = 0f
                                    var totalDragY = 0f
                                    detectDragGestures(
                                        onDrag = { change, dragAmount ->
                                            totalDragX += dragAmount.x
                                            totalDragY += dragAmount.y
                                        },
                                        onDragEnd = {val direction = when {
                                            kotlin.math.abs(totalDragX) > kotlin.math.abs(totalDragY) -> {
                                                if (totalDragX > 50) "RIGHT"
                                                else if (totalDragX < -50) "LEFT"
                                                else null
                                            }
                                            else -> {
                                                if (totalDragY > 50) "DOWN"
                                                else if (totalDragY < -50) "UP"
                                                else null
                                            }}
                                                    direction?.let{
                                                        viewModel.moveTileWithDirection(index, it)
                                                    }
                                                    totalDragX = 0f
                                            totalDragY = 0f
                                                   //viewModel.solvePuzze()

                                                    },

                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            if (tile != 0) {
                                Text(
                                    text = tile.toString(),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1565C0)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}