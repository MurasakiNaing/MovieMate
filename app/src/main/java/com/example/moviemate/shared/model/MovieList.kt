package com.example.moviemate.shared.model

data class MovieList(
    val page: Int,
    val results: List<MovieCardPreview>
)