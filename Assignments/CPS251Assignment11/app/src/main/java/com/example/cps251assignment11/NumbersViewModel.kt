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
import androidx.compose.runtime.mutableStateOf
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import com.example.cps251assignment11.ui.theme.CPS251Assignment11Theme
import kotlin.math.*
class NumbersViewModel : ViewModel(){
var tiles = mutableStateOf(generateShuffledTiles())
    private set
    var moves = mutableStateOf(0)
    private set
    private fun generateShuffledTiles(): List<Int>{
        return (1..8).plus(0).shuffled()
    }
    fun moveTile(index: Int){
        val currentTiles = tiles.value.toMutableList()
        val emptyIndex = currentTiles.indexOf(0)

        if(isAdjacent(index, emptyIndex)){
            currentTiles[emptyIndex] = currentTiles[index]
            currentTiles[index] = 0

            tiles.value = currentTiles
            moves.value += 1
        }
    }
    fun moveTileWithDirection(index: Int, direction: String) {
        val emptyIndex = tiles.value.indexOf(0)
        val row = index/3
        val col = index%3
        val targetIndex = when (direction) {
            "UP" -> if (row > 0) index - 3 else -1
            "DOWN" -> if (row < 2) index + 3 else -1
            "LEFT" -> if (col > 0) index - 1 else -1
            "RIGHT" -> if (col < 2) index + 1 else -1
            else -> -1
        }
        if(targetIndex == emptyIndex){
            moveTile(index)
        }
    }
    private fun isAdjacent(i1: Int, i2: Int): Boolean{
        val r1 = i1/3
        val c1 = i1 % 3
        val r2 = i2 /3
        val c2 = i2 %3
        return (abs(r1-r2) + abs(c1-c2)) == 1
    }
    fun resetGame() {
        tiles.value = generateShuffledTiles()
        moves.value = 0
    }

    fun isSolved(): Boolean{
        return tiles.value == listOf(1,2,3,4,5,6,7,8,0)
    }
    fun solvePuzze(){
        tiles.value = listOf(1,2,3,4,5,6,7,8,0)
    }
}