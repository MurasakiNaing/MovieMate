package com.example.moviemate.shared.utils

import android.content.Context
import android.content.Intent
import com.example.moviemate.details.ui.MovieDetailsActivity
import com.example.moviemate.shared.model.MovieCardPreview

object NavigationUtils {

    fun navigateToMovieDetails(context: Context, movie: MovieCardPreview) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra("MOVIE_ID", movie.id)
        context.startActivity(intent)
    }
}