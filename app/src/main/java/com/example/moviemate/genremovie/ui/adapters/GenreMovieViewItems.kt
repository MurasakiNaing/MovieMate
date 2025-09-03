package com.example.moviemate.genremovie.ui.adapters

import com.example.moviemate.shared.model.MovieCardPreview

sealed class GenreMovieViewItems {

    object GenreMovieTitle : GenreMovieViewItems()

    data class MovieCard(val movie: MovieCardPreview) : GenreMovieViewItems()
}