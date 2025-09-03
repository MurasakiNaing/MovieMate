package com.example.moviemate.shared.retrofit

import com.example.moviemate.details.model.CastDetailsList
import com.example.moviemate.home.model.GenreList
import com.example.moviemate.details.model.Movie
import com.example.moviemate.shared.model.MovieList
import com.example.moviemate.details.model.MovieReleaseDetailsList
import com.example.moviemate.details.model.MovieVideos
import com.example.moviemate.home.model.NowPlayingList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface TMdbApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("region") region: String = Locale.getDefault().country): MovieList

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int) : Movie

    @GET("movie/{movie_id}/release_dates")
    suspend fun getMovieReleaseDetails(@Path("movie_id") movieId: Int): MovieReleaseDetailsList

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCastsAndCrews(@Path("movie_id") movieId: Int): CastDetailsList

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): GenreList

    @GET("movie/now_playing")
    suspend fun getNowPlayingList(): NowPlayingList

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(@Path("time_window") timeWindow: String = "week"): MovieList

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("region") region: String = Locale.getDefault().country): MovieList

    @GET("discover/movie")
    suspend fun getMovies(@Query("page") page: Int, @Query("with_genres") genreId: String? = null): MovieList

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int): MovieList

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") movieId: Int): MovieVideos
}