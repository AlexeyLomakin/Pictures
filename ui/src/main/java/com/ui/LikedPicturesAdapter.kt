package com.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.domain.Picture
import com.ui.databinding.PictureElementBinding
import java.io.File

class LikedPicturesAdapter : ListAdapter<Picture, LikedPicturesAdapter.PictureViewHolder>(DiffCallback()) {

    private var onUnlikeClickListener: ((Picture) -> Unit)? = null

    inner class PictureViewHolder(private val binding: PictureElementBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(picture: Picture) {
            val localDirectoryPath = getLocalDirectoryPath()
            if (localDirectoryPath != null) {
                val file = File(localDirectoryPath, "${picture.id}.png")
                if (file.exists()) {
                    Glide.with(binding.pictureImageView.context)
                        .load(file)
                        .into(binding.pictureImageView)
                }
            }

            binding.likeButton.setOnClickListener {
                onUnlikeClickListener?.invoke(picture)
            }
        }

        private fun getLocalDirectoryPath(): String? {
            val directory = File(binding.pictureImageView.context.getExternalFilesDir(null), "saved_images")
            return if (directory.exists() || directory.mkdirs()) {
                directory.absolutePath
            } else {
                null
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


    fun setOnUnlikeClickListener(listener: (Picture) -> Unit) {
        onUnlikeClickListener = listener
    }

    private class DiffCallback : DiffUtil.ItemCallback<Picture>() {
        override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean =
            oldItem == newItem
    }
}
