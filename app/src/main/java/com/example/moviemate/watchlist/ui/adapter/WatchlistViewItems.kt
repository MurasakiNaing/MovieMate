package com.example.moviemate.watchlist.ui.adapter

import com.example.moviemate.shared.model.MovieCardPreview

sealed class WatchlistViewItems {

    object WatchListTitle : WatchlistViewItems()

    data class MovieCard(val movie: MovieCardPreview) : WatchlistViewItems()

}