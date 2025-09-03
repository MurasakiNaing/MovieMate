package com.example.moviemate.home.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.shared.model.Genre
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MovieRepository): ViewModel() {

    private var popularMoviesLiveData = MutableLiveData<List<MovieCardPreview>>()
    private var movieGenreListLiveData = MutableLiveData<List<Genre>>()

    private var nowPlayingLiveData = MutableLiveData<List<MovieCardPreview>>()

    private var trendingMoviesLiveData = MutableLiveData<List<MovieCardPreview>>()

    private var upcomingMoviesLiveData = MutableLiveData<List<MovieCardPreview>>()

    fun fetchPopularMovies() {
        viewModelScope.launch {
            repository.getPopularMovies().onSuccess {
                popularMoviesLiveData.postValue(it.results)
            }.onFailure {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
            }
        }
    }

    fun fetchMovieGenres() {
        viewModelScope.launch {
            repository.getMovieGenres().onSuccess {
                movieGenreListLiveData.postValue(it.genres)
            }.onFailure {
                Log.e("Home fragment genres", it.message.toString())
                it.printStackTrace()
            }
        }
    }

    fun fetchNowPlayingList() {
        viewModelScope.launch {
            repository.getNowPlayingList().onSuccess {
                nowPlayingLiveData.postValue(it.results)
            }.onFailure {
                Log.e("Home fragment now playing", it.message.toString())
                it.printStackTrace()
            }
        }
    }

    fun fetchTrendingMovies() {
        viewModelScope.launch {
            repository.getTrendingMovies().onSuccess {
                trendingMoviesLiveData.postValue(it.results)
            }.onFailure {
                Log.e("Home fragment trending", it.message.toString())
                it.printStackTrace()
            }
        }
    }

    fun fetchUpcomingMovies() {
        viewModelScope.launch {
            repository.getUpcomingMovies().onSuccess {
                upcomingMoviesLiveData.postValue(it.results)
            }.onFailure {
                Log.e("Home fragment upcoming", it.message.toString())
                it.printStackTrace()
            }
        }
    }

    fun observePopularMoviesLiveData(): LiveData<List<MovieCardPreview>> {
        return popularMoviesLiveData
    }

    fun observeMovieGenreListLiveData(): LiveData<List<Genre>> {
        return movieGenreListLiveData
    }

    fun observeNowPlayingLiveData(): LiveData<List<MovieCardPreview>> {
        return nowPlayingLiveData
    }

    fun observeTrendingMovieLiveData(): LiveData<List<MovieCardPreview>> {
        return trendingMoviesLiveData
    }

    fun observeUpcomingMovieLiveData(): LiveData<List<MovieCardPreview>> {
        return upcomingMoviesLiveData
    }

}