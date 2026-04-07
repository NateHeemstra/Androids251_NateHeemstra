package com.example.assignment9

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.navigation.NavController
import com.example.assignment9.api.OmdbMovieDetailResponse

@Composable
fun MovieDetailsScreen(
    navController: NavController,
    imdbID: String,
    viewModel: MovieViewModel
) {

    var movie by remember { mutableStateOf<OmdbMovieDetailResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(imdbID) {
        movie = viewModel.getMovieDetailsById(imdbID)
        isLoading = false
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        movie?.let { m ->

            AsyncImage(
                model = m.poster,
                contentDescription = "Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(m.title ?: "No title")
            Text("Year: ${m.year}")
            Text("Rated: ${m.rated}")
            Text("Director: ${m.director}")
            Text("IMDb: ${m.imdbRating}")
            Text("Box Office: ${m.boxOffice}")

            val rotten = m.ratings?.find { it.source == "Rotten Tomatoes" }
            Text("Rotten Tomatoes: ${rotten?.value ?: "N/A"}")

            Spacer(modifier = Modifier.height(12.dp))

            Text(m.plot ?: "No plot")
        }
    }}