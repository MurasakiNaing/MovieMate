package com.example.moviemate.genremovie.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.example.moviemate.genremovie.ui.adapters.GenreMovieViewItems
import com.example.moviemate.shared.repository.MovieRepository
import kotlinx.coroutines.flow.map

class GenreMoviesViewModel(private val repository: MovieRepository, private val genreId: Int) :
    ViewModel() {

    val movies = repository.getGenreMoviesPager(genreId).map {
        it.map { movie ->
            GenreMovieViewItems.MovieCard(movie) as GenreMovieViewItems
        }
            .insertHeaderItem(item = GenreMovieViewItems.GenreMovieTitle)
    }.cachedIn(viewModelScope)

}