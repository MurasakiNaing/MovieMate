package com.example.moviemate.details.model

data class MovieReleaseDetails(
    val iso_3166_1: String,
    val release_dates: List<ReleaseDate>
)