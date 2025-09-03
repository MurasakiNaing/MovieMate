package com.example.moviemate.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.MovieRecyclerRowBinding

class MovieSectionAdapter(private val title: String, private var contentAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) : RecyclerView.Adapter<MovieSectionAdapter.MovieSectionViewHolder>() {

    fun updateAdapter(newAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        this.contentAdapter = newAdapter
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieSectionViewHolder {
        return MovieSectionViewHolder(MovieRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: MovieSectionViewHolder,
        position: Int
    ) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1

    inner class MovieSectionViewHolder(val binding: MovieRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tvSectionTitle.text = title
            binding.rvMoviesRow.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = contentAdapter
            }
        }
    }

}