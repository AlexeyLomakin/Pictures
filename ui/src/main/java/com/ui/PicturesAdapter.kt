package com.example.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.red
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.domain.Picture
import com.ui.R
import com.ui.databinding.PictureElementBinding

class PicturesAdapter : ListAdapter<Picture, PicturesAdapter.PictureViewHolder>(DiffCallback()) {

    private var onLikeClickListener: ((Picture) -> Unit)? = null
    private var isFavoriteScreen: Boolean = false

    inner class PictureViewHolder(private val binding: PictureElementBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(picture: Picture) {
            Glide.with(binding.pictureImageView.context)
                .load(picture.download_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.pictureImageView)

            binding.likeButton.visibility = View.VISIBLE
            binding.likeButton.setImageResource(
                if (isFavoriteScreen) R.drawable.like else R.drawable.like.apply {
                    ContextCompat.getColor(binding.likeButton.context, R.color.colorError)
                }
            )

            binding.likeButton.setOnClickListener {
                onLikeClickListener?.invoke(picture)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PictureElementBinding.inflate(inflater, parent, false)
        return PictureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<Picture>() {
        override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean =
            oldItem == newItem
    }
    fun setOnLikeClickListener(listener: (Picture) -> Unit) {
        onLikeClickListener = listener
    }
}
