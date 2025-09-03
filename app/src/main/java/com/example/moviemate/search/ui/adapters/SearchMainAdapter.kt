package com.example.moviemate.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.MovieCardBinding
import com.example.moviemate.databinding.SearchBarLayoutBinding
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.viewholders.MovieCardViewHolder


class SearchMainAdapter(private val onQueryChanged: (String) -> Unit) : PagingDataAdapter<SearchViewItems, RecyclerView.ViewHolder>(
    SearchMovieDiffCallback()
) {

    lateinit var onClick: ((MovieCardPreview) -> Unit)

    companion object {
        const val VIEW_TYPE_SEARCH = 0
        const val VIEW_TYPE_MOVIE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is SearchViewItems.SearchBar -> VIEW_TYPE_SEARCH
            is SearchViewItems.MovieCard -> VIEW_TYPE_MOVIE
            else -> -1
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_SEARCH -> {
                val binding = SearchBarLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SearchBarViewHolder(binding, onQueryChanged)
            }
            VIEW_TYPE_MOVIE -> {
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
            is SearchBarViewHolder -> holder.bind()
            is MovieCardViewHolder -> {
                val item = getItem(position) as SearchViewItems.MovieCard
                holder.bind(item.movie)
            }
        }
    }

    class SearchMovieDiffCallback : DiffUtil.ItemCallback<SearchViewItems>() {
        override fun areItemsTheSame(oldItem: SearchViewItems, newItem: SearchViewItems): Boolean {
            return when {
                oldItem is SearchViewItems.SearchBar && newItem is SearchViewItems.SearchBar -> true
                oldItem is SearchViewItems.MovieCard && newItem is SearchViewItems.MovieCard ->
                    oldItem.movie.id == newItem.movie.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: SearchViewItems, newItem: SearchViewItems): Boolean {
            return oldItem == newItem
        }

    }
}