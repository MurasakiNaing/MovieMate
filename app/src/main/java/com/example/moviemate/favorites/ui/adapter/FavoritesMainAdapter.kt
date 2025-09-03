package com.example.moviemate.favorites.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.R
import com.example.moviemate.databinding.FavoritesTvLayoutBinding
import com.example.moviemate.databinding.MovieCardBinding
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.viewholders.MovieCardViewHolder

class FavoritesMainAdapter : ListAdapter<FavoritesViewItems, RecyclerView.ViewHolder>(
    FavoritesDiffCallback()
){
    lateinit var onClick: ((MovieCardPreview) -> Unit)

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is FavoritesViewItems.FavoritesViewTitle -> R.layout.favorites_tv_layout
            is FavoritesViewItems.MovieCard -> R.layout.movie_card
            else -> throw IllegalStateException("Item at position $position is null")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.favorites_tv_layout -> {
                val binding = FavoritesTvLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FavoritesTitleViewHolder(binding)
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
            is FavoritesTitleViewHolder -> {}
            is MovieCardViewHolder -> {
                val item = getItem(position) as FavoritesViewItems.MovieCard
                holder.bind(item.movie)
            }
        }
    }

    class FavoritesDiffCallback() : DiffUtil.ItemCallback<FavoritesViewItems> () {
        override fun areItemsTheSame(
            oldItem: FavoritesViewItems,
            newItem: FavoritesViewItems
        ): Boolean {
            return when {
                oldItem is FavoritesViewItems.FavoritesViewTitle && newItem is FavoritesViewItems.FavoritesViewTitle -> true
                oldItem is FavoritesViewItems.MovieCard && newItem is FavoritesViewItems.MovieCard -> true
                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: FavoritesViewItems,
            newItem: FavoritesViewItems
        ): Boolean = oldItem == newItem
    }


}