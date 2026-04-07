package com.example.assignment9.api

import com.google.gson.annotations.SerializedName
data class OmdbResponse(
    @SerializedName("Search") val search: List<MovieSearchItem>?,
    @SerializedName("Response") val response: String?,
    @SerializedName("Error") val error: String?
)
data class MovieSearchItem(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("imdbID") val imdbID: String?,
    @SerializedName("Poster") val poster: String?
)
data class OmdbMovieDetailResponse(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("Rated") val rated: String?,
    @SerializedName("Director") val director: String?,
    @SerializedName("Actors") val actors: String?,
    @SerializedName("Plot") val plot: String?,
    @SerializedName("Poster") val poster: String?,
    @SerializedName("BoxOffice") val boxOffice: String?,
    @SerializedName("imdbRating") val imdbRating: String?,
    @SerializedName("Ratings") val ratings: List<Rating>?,
    @SerializedName("Response") val response: String?
)
data class  Rating(
    @SerializedName("Source") val source: String?,
    @SerializedName("Valuse") val value: String?
)