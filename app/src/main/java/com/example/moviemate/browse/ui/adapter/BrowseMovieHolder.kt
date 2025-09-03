package com.example.moviemate.browse.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.BrowseTvLayoutBinding

class BrowseTextViewHolder(val binding: BrowseTvLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.tvBrowseMovie.text = "Browse Movies"
    }
}