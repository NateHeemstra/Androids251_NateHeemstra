package com.example.assignment9
import com.example.assignment9.api.OmdbResponse
import com.example.assignment9.api.RetrofitInstance
import com.example.assignment9.api.OmdbMovieDetailResponse

import androidx.compose.runtime.*
class MovieRepository {
    private val api = RetrofitInstance.api
    private val apiKey = "23ffb6b3"
    suspend fun searchMovies(query: String): Result<OmdbResponse>{
        return try{
            val response = api.searchMovies(query, apiKey)
            Result.success(response)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun getMovieDetails(imdbId: String): Result<OmdbMovieDetailResponse> {
        return try {
            val response = api.getMovieDetails(imdbId, apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}