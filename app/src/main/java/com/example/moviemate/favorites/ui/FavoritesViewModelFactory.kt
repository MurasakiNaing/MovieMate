package com.example.moviemate.favorites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviemate.shared.repository.MovieRepository
import com.example.moviemate.shared.retrofit.TMdbApiService

class FavoritesViewModelFactory(private val apiService: TMdbApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = MovieRepository(apiService)
        return FavoritesViewModel(repository) as T
    }
}