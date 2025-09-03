package com.example.moviemate.home.model

import com.example.moviemate.shared.model.MovieCardPreview

data class NowPlayingList(
    val results: List<MovieCardPreview>
)