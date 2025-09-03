package com.example.moviemate.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.HomeGenreSectionBinding
import com.example.moviemate.databinding.MainMoviesRecyclerBinding

class HomeMainRecyclerAdapter(private var genreAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>, private val concatAdapter: ConcatAdapter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val GENRE_SECTION = 0
        const val MOVIE_SECTION = 1
    }

    fun updateGenreAdapter(newAdapter: GenreAdapter) {
        genreAdapter = newAdapter
        notifyItemChanged(0)
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            GENRE_SECTION -> GENRE_SECTION
            MOVIE_SECTION -> MOVIE_SECTION
            else -> -1
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            GENRE_SECTION -> {
                val binding = HomeGenreSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GenreSectionViewHolder(binding)
            }
            MOVIE_SECTION -> {
                val binding = MainMoviesRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MainMovieRecyclerViewHolder(binding)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(holder) {
            is GenreSectionViewHolder -> holder.bind(genreAdapter)
            is MainMovieRecyclerViewHolder -> holder.bind(concatAdapter)
        }
    }

    override fun getItemCount(): Int = 2
}