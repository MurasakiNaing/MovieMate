package com.example.moviemate.search.ui.adapters

import com.example.moviemate.shared.model.MovieCardPreview

sealed class SearchViewItems {
    object SearchBar : SearchViewItems()
    data class MovieCard(val movie: MovieCardPreview) : SearchViewItems()
}