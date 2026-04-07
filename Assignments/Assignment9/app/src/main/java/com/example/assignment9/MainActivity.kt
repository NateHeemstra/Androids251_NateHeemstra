package com.example.assignment9

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment9.ui.theme.Assignment9Theme
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import com.example.assignment9.api.MovieSearchItem
import com.example.assignment9.api.OmdbMovieDetailResponse
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent {
            Assignment9Theme() {
                AppNavigation()
            }
        }
    }
}
@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val repository = MovieRepository()
    val movieViewModel: MovieViewModel = viewModel(
        factory = MovieViewModel.provideFactory(repository)
    )

    NavHost(navController = navController, startDestination = "search"){
        composable("search"){
            MovieSearchScreen(navController, movieViewModel)
        }
        composable("movie_details/{imdbID}"){backStackEntry ->
            val imdbID = backStackEntry.arguments?.getString("imdbID") ?:""

            MovieDetailsScreen(
                navController = navController,
                imdbID = imdbID,
                viewModel=movieViewModel
            )
        }
    }
}
/**
 * Explain the role of Retrofit in this project. How does it simplify interacting with the OMDB API compared to making raw HTTP requests?
 * What is the purpose of the data classes (e.g., OmdbMovieDetailResponse, MovieSearchItem) in this project? How do they relate to the JSON responses from the OMDB API?
 * Why is a MovieRepository included in this project's architecture? What are the benefits of having a separate repository layer for data fetching, especially in a larger application?
 * What are the primary responsibilities of the MovieViewModel? How does it help to separate concerns between the UI and the data layer, and what advantages does this provide?
 * Describe how navigation between the MovieSearchScreen and MovieDetailsScreen is handled in this project. How are arguments (like imdbId) passed between composables during navigation?
 */