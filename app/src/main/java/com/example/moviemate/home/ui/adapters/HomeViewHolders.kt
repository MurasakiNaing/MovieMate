package com.example.moviemate.home.ui.adapters

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.HomeGenreSectionBinding
import com.example.moviemate.databinding.MainMoviesRecyclerBinding

class GenreSectionViewHolder(val binding: HomeGenreSectionBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(genreAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        binding.rvGenres.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }
}

class MainMovieRecyclerViewHolder(val binding: MainMoviesRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(concatAdapter: ConcatAdapter) {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = concatAdapter
        }
    }
}