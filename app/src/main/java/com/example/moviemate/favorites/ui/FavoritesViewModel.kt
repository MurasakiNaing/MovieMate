package com.example.moviemate.favorites.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.favorites.ui.adapter.FavoritesViewItems
import com.example.moviemate.shared.firestore.FireStoreService
import com.example.moviemate.shared.firestore.FirebaseAuthService
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.repository.MovieRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: MovieRepository) : ViewModel() {

    private var favoriteItemsLiveData = MutableLiveData<List<FavoritesViewItems>>()

    init {
        listenToFavorites()
    }

    fun getFavoriteMovies() {
        FirebaseAuthService.getCurrentUser()?.uid?.let {
            viewModelScope.launch {
                val movies = FireStoreService.fetchUserFavorites(it)
                val items = mutableListOf<FavoritesViewItems>()

                items.add(FavoritesViewItems.FavoritesViewTitle)
                if (movies.isNotEmpty()) {
                    movies.forEach {
                        items.add(FavoritesViewItems.MovieCard(it))
                    }
                }
                favoriteItemsLiveData.postValue(items)
            }
        }
    }

    private fun listenToFavorites() {
        FireStoreService.listenFavorites {movies ->
            val items = mutableListOf<FavoritesViewItems>(FavoritesViewItems.FavoritesViewTitle)
            if (movies.isNotEmpty()) {
                movies.forEach {
                    items.add(FavoritesViewItems.MovieCard(it))
                }
            }
            favoriteItemsLiveData.postValue(items)
        }
    }

    fun observeFavoriteItemsLiveData(): LiveData<List<FavoritesViewItems>> {
        return favoriteItemsLiveData
    }

}