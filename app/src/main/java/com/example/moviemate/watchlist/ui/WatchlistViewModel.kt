package com.example.moviemate.watchlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.shared.firestore.FireStoreService
import com.example.moviemate.watchlist.ui.adapter.WatchlistViewItems
import kotlinx.coroutines.launch

class WatchlistViewModel : ViewModel() {

    private var watchlistMoviesLiveData = MutableLiveData<List<WatchlistViewItems>>()

    init {
        listenForWatchlist()
    }

    fun getWatchlistMovies() {
        viewModelScope.launch {
            val movies = FireStoreService.fetchUserWatchlist()
            val items = mutableListOf<WatchlistViewItems>(WatchlistViewItems.WatchListTitle)

            if (movies.isNotEmpty()) {
                movies.forEach {movie ->
                    items.add(WatchlistViewItems.MovieCard(movie))
                }
            }

            watchlistMoviesLiveData.postValue(items)
        }
    }

    private fun listenForWatchlist() {
        FireStoreService.listenWatchlist { movies ->
            val items = mutableListOf<WatchlistViewItems>(WatchlistViewItems.WatchListTitle)

            if (movies.isNotEmpty()) {
                movies.forEach {movie ->
                    items.add(WatchlistViewItems.MovieCard(movie))
                }
            }

            watchlistMoviesLiveData.postValue(items)
        }
    }

    fun observeWatchListMoviesLiveData() : LiveData<List<WatchlistViewItems>> {
        return watchlistMoviesLiveData
    }


}