package com.example.moviemate.shared.shimmer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.ShimmerMovieCardLayoutBinding

class ShimmerMovieCardAdapter(private val itemCount : Int) : RecyclerView.Adapter<ShimmerMovieCardAdapter.ShimmerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShimmerViewHolder {
        val binding = ShimmerMovieCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShimmerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ShimmerViewHolder,
        position: Int
    ) {}

    override fun getItemCount(): Int = itemCount

    inner class ShimmerViewHolder(binding: ShimmerMovieCardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}