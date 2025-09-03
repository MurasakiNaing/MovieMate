package com.example.moviemate.genremovie.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.GenreMoviesTvLayoutBinding

class GenreMoviesTitleHolder(private val binding: GenreMoviesTvLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(genreTitle: String) {
        binding.tvGenreTitle.text = genreTitle
    }

}