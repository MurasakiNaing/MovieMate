package com.example.moviemate.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemate.databinding.MovieCardBinding
import com.example.moviemate.shared.model.MovieCardPreview

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieAdapterViewHolder>() {

    private var moviesList = ArrayList<MovieCardPreview>()
    lateinit var onClick: ((MovieCardPreview) -> Unit)

    fun setMovies(moviesList: List<MovieCardPreview>) {
        this.moviesList = moviesList as ArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapterViewHolder {
        return MovieAdapterViewHolder(MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: MovieAdapterViewHolder,
        position: Int
    ) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500/${moviesList[position].poster_path}")
            .into(holder.binding.imageMoviePoster)
        holder.itemView.setOnClickListener {
            onClick.invoke(moviesList[position])
        }
    }

    override fun getItemCount(): Int = moviesList.size

    class MovieAdapterViewHolder(val binding: MovieCardBinding): RecyclerView.ViewHolder(binding.root)

}