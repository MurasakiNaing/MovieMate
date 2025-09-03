package com.example.moviemate.details.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.details.model.Cast
import com.example.moviemate.details.model.Movie
import com.example.moviemate.details.model.ReleaseDate
import com.example.moviemate.shared.firestore.FireStoreService
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.repository.MovieRepository
import kotlinx.coroutines.launch
import java.util.Locale

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    private var movieDetailsLiveData = MutableLiveData<Movie>()
    private var movieReleaseDetailsLiveData = MutableLiveData<ReleaseDate>()
    private var movieTrailerLiveData = MutableLiveData<String>()

    private var castListLiveData = MutableLiveData<List<Cast>>()
    private var favoriteMoviesLiveData = MutableLiveData(mutableListOf<MovieCardPreview>())
    private var watchlistMoviesLiveData = MutableLiveData(mutableListOf<MovieCardPreview>())

    init {
        listenToFavorites()
        listenToWatchlist()
    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val result = repository.getMovieDetails(movieId)
            result.onSuccess {
                movieDetailsLiveData.postValue(it)
            }.onFailure {
                Log.e("Movie details", it.message.toString())
                it.printStackTrace()
            }
        }
    }

    fun fetchMovieReleaseDetails(movieId: Int) {
        viewModelScope.launch {
            val result = repository.getMovieReleaseDetails(movieId)
            result.onSuccess {
                val countryCode = Locale.getDefault().country
                val match = it.results.firstOrNull{ movieReleaseDate ->
                    movieReleaseDate.iso_3166_1.equals(countryCode, ignoreCase = true)
                }
                val release = match?.release_dates?.firstOrNull()
                if (release != null) {
                   movieReleaseDetailsLiveData.postValue(release)
                } else {
                    movieReleaseDetailsLiveData.postValue(ReleaseDate("Not available", "Not available"))
                }
            }
        }
    }

    fun fetchMovieTrailer(movieId: Int) {
        viewModelScope.launch {
            val result = repository.getMovieVideos(movieId)
            result.onSuccess { it ->
                val trailer = it.results.firstOrNull { video ->
                    video.type == "Trailer"
                }
                trailer?.let {
                    movieTrailerLiveData.postValue(it.key)
                }
            }
        }
    }

    fun fetchMovieCastList(movieId: Int) {
        viewModelScope.launch {
            val result = repository.getMovieCasts(movieId)
            result.onSuccess {
                castListLiveData.postValue(it.cast)
            }.onFailure {
                Log.e("Movie details casts", it.message.toString())
                it.printStackTrace()
            }
        }
    }

    fun toggleFavorites(movie: MovieCardPreview, context: Context) {
        val isAdd = if (favoriteMoviesLiveData.value.contains(movie)) {
            favoriteMoviesLiveData.value.remove(movie)
            false
        } else {
            favoriteMoviesLiveData.value.add(movie)
        }
        try {
            viewModelScope.launch {
                val res = FireStoreService.toggleFavorite(movie, isAdd)
                when(res) {
                    0 -> Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error adding movie to favorites", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun toggleWatchlist(movie: MovieCardPreview, context: Context) {
        val isAdd = if (watchlistMoviesLiveData.value.contains(movie)) {
            watchlistMoviesLiveData.value.remove(movie)
            false
        } else {
            watchlistMoviesLiveData.value.add(movie)
        }

        try {
            viewModelScope.launch {
                val res = FireStoreService.toggleWatchlist(movie, isAdd)
                when(res) {
                    0 -> Toast.makeText(context, "Removed from watchlist", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(context, "Added to watchlist", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error adding movie to watchlist", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun observeMovieCastList() : LiveData<List<Cast>> {
        return castListLiveData
    }

    fun observeMovieReleaseDetailsLiveData() : LiveData<ReleaseDate> {
        return movieReleaseDetailsLiveData
    }

    fun observeMovieDetailsLiveData(): LiveData<Movie> {
        return movieDetailsLiveData
    }

    fun observeMovieTrailerLiveData(): LiveData<String> {
        return  movieTrailerLiveData
    }

    fun isMovieFavorite(movieId: Int): Boolean {
        return favoriteMoviesLiveData.value.any{ it.id == movieId } ?: false
    }

    fun isMovieWatchlist(movieId: Int): Boolean {
        return watchlistMoviesLiveData.value.any { it.id == movieId} ?: false
    }

    private fun listenToFavorites() {
        FireStoreService.listenFavorites {movies ->
            favoriteMoviesLiveData.postValue(movies.toMutableList())
        }
    }

    private fun listenToWatchlist() {
        FireStoreService.listenWatchlist { movies ->
            watchlistMoviesLiveData.postValue(movies.toMutableList())
        }
    }

}