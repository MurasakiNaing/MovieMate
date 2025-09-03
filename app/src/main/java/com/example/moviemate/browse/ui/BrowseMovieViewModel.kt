package com.example.moviemate.browse.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviemate.shared.repository.MovieRepository

class BrowseMovieViewModel(repository: MovieRepository) : ViewModel() {

    val movies = repository.getBrowseMoviePager().cachedIn(viewModelScope)

}