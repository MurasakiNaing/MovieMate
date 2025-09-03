package com.example.moviemate.genremovie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviemate.shared.repository.MovieRepository
import com.example.moviemate.shared.retrofit.TMdbApiService

class GenreMoviesViewModelFactory(private val apiService: TMdbApiService, private val genreId: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = MovieRepository(apiService)
        return GenreMoviesViewModel(repository, genreId) as T
    }

}