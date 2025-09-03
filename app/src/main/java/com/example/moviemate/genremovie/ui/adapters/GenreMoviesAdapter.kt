package com.example.moviemate.genremovie.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.R
import com.example.moviemate.databinding.GenreMoviesTvLayoutBinding
import com.example.moviemate.databinding.MovieCardBinding
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.viewholders.MovieCardViewHolder

class GenreMoviesAdapter(private val genreTitle: String) : PagingDataAdapter<GenreMovieViewItems, RecyclerView.ViewHolder>(
    GenreMovieDiffCallback()
) {

    lateinit var onClick: ((MovieCardPreview) -> Unit)

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is GenreMovieViewItems.GenreMovieTitle -> R.layout.genre_movies_tv_layout
            is GenreMovieViewItems.MovieCard -> R.layout.movie_card
            null -> throw IllegalStateException("Item at position $position is null")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.genre_movies_tv_layout -> {
                val binding = GenreMoviesTvLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GenreMoviesTitleHolder(binding)
            }
            R.layout.movie_card -> {
                val binding = MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieCardViewHolder(binding, onClick)
            }
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(holder) {
            is GenreMoviesTitleHolder -> holder.bind(genreTitle)
            is MovieCardViewHolder -> {
                val item = getItem(position) as GenreMovieViewItems.MovieCard
                holder.bind(item.movie)
            }
        }
    }

    class GenreMovieDiffCallback : DiffUtil.ItemCallback<GenreMovieViewItems>() {
        override fun areItemsTheSame(
            oldItem: GenreMovieViewItems,
            newItem: GenreMovieViewItems
        ): Boolean {
            return when {
                oldItem is GenreMovieViewItems.GenreMovieTitle && newItem is GenreMovieViewItems.GenreMovieTitle -> true
                oldItem is GenreMovieViewItems.MovieCard && newItem is GenreMovieViewItems.MovieCard ->
                    oldItem.movie.id  == newItem.movie.id
                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: GenreMovieViewItems,
            newItem: GenreMovieViewItems
        ): Boolean = oldItem == newItem

    }
}