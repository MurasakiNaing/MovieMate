package com.example.moviemate.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.GenreCardLayoutBinding
import com.example.moviemate.shared.model.Genre

class GenreAdapter() : ListAdapter<Genre, GenreAdapter.GenreViewHolder>(GenreDiffUtil()) {

    lateinit var onClick: ((Genre) -> Unit)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenreViewHolder {
        return GenreViewHolder(GenreCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: GenreViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class GenreViewHolder(val binding: GenreCardLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre) {
            binding.tvGenreCard.text = genre.name
            itemView.setOnClickListener {
                onClick.invoke(genre)
            }
        }
    }

    class GenreDiffUtil : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(
            oldItem: Genre,
            newItem: Genre
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Genre,
            newItem: Genre
        ): Boolean {
            return oldItem == newItem
        }

    }

}