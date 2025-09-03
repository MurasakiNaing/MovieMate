package com.example.moviemate.shared.shimmer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.ShimmerGenreCardLayoutBinding

class ShimmerGenreCardAdapter(private val itemCount: Int) : RecyclerView.Adapter<ShimmerGenreCardAdapter.ShimmerGenreCardViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShimmerGenreCardAdapter.ShimmerGenreCardViewHolder {
        val binding = ShimmerGenreCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShimmerGenreCardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ShimmerGenreCardAdapter.ShimmerGenreCardViewHolder,
        position: Int
    ) {
    }

    override fun getItemCount(): Int = itemCount

    inner class ShimmerGenreCardViewHolder(binding: ShimmerGenreCardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}
}