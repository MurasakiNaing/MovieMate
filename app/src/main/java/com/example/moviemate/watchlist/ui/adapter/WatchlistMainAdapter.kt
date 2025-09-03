package com.example.moviemate.watchlist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.R
import com.example.moviemate.databinding.MovieCardBinding
import com.example.moviemate.databinding.WatchlistTvLayoutBinding
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.viewholders.MovieCardViewHolder

class WatchlistMainAdapter : ListAdapter<WatchlistViewItems, RecyclerView.ViewHolder>(
    WatchlistDiffCallback()
) {

    lateinit var onClick: ((MovieCardPreview) -> Unit)

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is WatchlistViewItems.WatchListTitle -> R.layout.watchlist_tv_layout
            is WatchlistViewItems.MovieCard -> R.layout.movie_card
            else -> throw IllegalStateException("Item at position $position is null")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.watchlist_tv_layout -> {
                val binding = WatchlistTvLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WatchlistTitleViewHolder(binding)
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
            is WatchlistTitleViewHolder -> {}
            is MovieCardViewHolder -> {
                val item = getItem(position) as WatchlistViewItems.MovieCard
                holder.bind(item.movie)
            }
        }
    }

    class WatchlistDiffCallback() : DiffUtil.ItemCallback<WatchlistViewItems>() {
        override fun areItemsTheSame(
            oldItem: WatchlistViewItems,
            newItem: WatchlistViewItems
        ): Boolean {
            return when {
                oldItem is WatchlistViewItems.WatchListTitle && newItem is WatchlistViewItems.WatchListTitle -> true
                oldItem is WatchlistViewItems.MovieCard && newItem is WatchlistViewItems.MovieCard -> true
                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: WatchlistViewItems,
            newItem: WatchlistViewItems
        ): Boolean = oldItem == newItem

    }
}