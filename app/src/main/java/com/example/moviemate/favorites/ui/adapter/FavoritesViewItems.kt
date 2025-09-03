package com.example.moviemate.favorites.ui.adapter

import com.example.moviemate.shared.model.MovieCardPreview

sealed class FavoritesViewItems {

    object FavoritesViewTitle : FavoritesViewItems()

    data class MovieCard(val movie: MovieCardPreview) : FavoritesViewItems()
}