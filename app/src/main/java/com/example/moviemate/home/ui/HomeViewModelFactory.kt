package com.example.moviemate.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviemate.shared.repository.MovieRepository
import com.example.moviemate.shared.retrofit.TMdbApiService

class HomeViewModelFactory(private val apiService: TMdbApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = MovieRepository(apiService)
        return HomeViewModel(repository) as T
    }
}