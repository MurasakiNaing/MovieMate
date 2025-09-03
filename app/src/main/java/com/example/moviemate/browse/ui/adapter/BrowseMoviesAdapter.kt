package com.example.moviemate.browse.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.BrowseTvLayoutBinding
import com.example.moviemate.databinding.MovieCardBinding
import com.example.moviemate.shared.model.MovieCardPreview
import com.example.moviemate.shared.viewholders.MovieCardViewHolder

class BrowseMoviesAdapter :
    PagingDataAdapter<MovieCardPreview, RecyclerView.ViewHolder>(
        BrowseMovieDiffCallback()
    ) {
    lateinit var onClick: ((MovieCardPreview) -> Unit)

    companion object {
        const val TV_BROWSE_MOVIES = 0
        const val BROWSE_MOVIES = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            TV_BROWSE_MOVIES -> TV_BROWSE_MOVIES
            BROWSE_MOVIES -> BROWSE_MOVIES
            else -> -1
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if(viewType == 0) {
                val binding = BrowseTvLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BrowseTextViewHolder(binding)
        } else {
            val binding = MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieCardViewHolder(binding, onClick)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(holder) {
            is BrowseTextViewHolder -> holder.bind()
            is MovieCardViewHolder -> holder.bind(getItem(position)!!)
        }
    }
    class BrowseMovieDiffCallback : DiffUtil.ItemCallback<MovieCardPreview>() {
        override fun areItemsTheSame(
            oldItem: MovieCardPreview,
            newItem: MovieCardPreview
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MovieCardPreview,
            newItem: MovieCardPreview
        ): Boolean = oldItem == newItem

    }

}