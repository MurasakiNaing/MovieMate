package com.example.moviemate.shared.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemate.R
import com.example.moviemate.databinding.MovieCardBinding
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.utils.Constants

class MovieCardViewHolder(private val binding: MovieCardBinding, private val onClick: (MovieCardPreview) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: MovieCardPreview) {
        Glide.with(itemView)
            .load(Constants.IMAGE_BASE_PATH + movie.poster_path)
            .override(200, 250)
            .placeholder(R.drawable.placeholder)
            .into(binding.imageMoviePoster)

        itemView.setOnClickListener {
            onClick.invoke(movie)
        }
    }
}