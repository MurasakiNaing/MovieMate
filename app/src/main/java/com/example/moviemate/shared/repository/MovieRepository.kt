package com.example.moviemate.shared.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviemate.shared.paging.MoviePagingSource
import com.example.moviemate.details.model.CastDetailsList
import com.example.moviemate.home.model.GenreList
import com.example.moviemate.details.model.Movie
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.model.MovieList
import com.example.moviemate.details.model.MovieReleaseDetailsList
import com.example.moviemate.details.model.MovieVideos
import com.example.moviemate.home.model.NowPlayingList
import com.example.moviemate.shared.retrofit.TMdbApiService
import com.example.moviemate.shared.utils.Constants
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val apiService: TMdbApiService) {

    suspend fun getPopularMovies(): Result<MovieList> {
        return try {
            val response = apiService.getPopularMovies()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val response = apiService.getMovieDetails(movieId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieReleaseDetails(movieId: Int): Result<MovieReleaseDetailsList> {
        return try {
            val response = apiService.getMovieReleaseDetails(movieId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieCasts(movieId: Int): Result<CastDetailsList> {
        return try {
            val response = apiService.getMovieCastsAndCrews(movieId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieGenres(): Result<GenreList> {
        return try {
            val response = apiService.getMovieGenres()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNowPlayingList(): Result<NowPlayingList> {
        return try {
            val response = apiService.getNowPlayingList()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrendingMovies(): Result<MovieList> {
        return try {
            val response = apiService.getTrendingMovies()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUpcomingMovies(): Result<MovieList> {
        return try {
            val response = apiService.getUpcomingMovies()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieVideos(movieId: Int): Result<MovieVideos> {
        return try {
            val response = apiService.getMovieVideos(movieId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getBrowseMoviePager(): Flow<PagingData<MovieCardPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                maxSize = 60,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                MoviePagingSource(
                    apiService = apiService,
                    mode = Constants.Mode.Browse
                )
            }).flow
    }

    fun getSearchMoviePager(query: String): Flow<PagingData<MovieCardPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                maxSize = 60,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                MoviePagingSource(apiService = apiService, mode = Constants.Mode.Search, query = query)
            }).flow
    }

    fun getGenreMoviesPager(genreId: Int): Flow<PagingData<MovieCardPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                maxSize = 60,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                MoviePagingSource(apiService = apiService, mode = Constants.Mode.Browse, genreId = genreId)
            }).flow
    }

}