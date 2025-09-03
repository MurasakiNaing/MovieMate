package com.example.moviemate.search.ui.adapters

import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.databinding.SearchBarLayoutBinding

class SearchBarViewHolder(
    private val binding: SearchBarLayoutBinding,
    private val onQueryChanged: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.editTextSearch.addTextChangedListener {
            onQueryChanged(it.toString())
        }
    }
}