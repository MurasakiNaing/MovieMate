package com.example.moviemate.details.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemate.databinding.CastCardBinding
import com.example.moviemate.details.model.Cast
import com.example.moviemate.shared.utils.Constants

class CastAdapter : ListAdapter<Cast, CastAdapter.CastAdapterViewHolder>(CastDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CastAdapterViewHolder {
        return CastAdapterViewHolder(
            CastCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CastAdapterViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class CastAdapterViewHolder(val binding: CastCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            Glide.with(itemView)
                .load(Constants.IMAGE_BASE_PATH + cast.profile_path)
                .into(binding.imageCast)

            binding.tvCastName.text = cast.name
            binding.tvCharacterName.text = cast.character
        }
    }

    class CastDiffCallback : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(
            oldItem: Cast,
            newItem: Cast
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Cast,
            newItem: Cast
        ): Boolean {
            return oldItem == newItem
        }

    }

}