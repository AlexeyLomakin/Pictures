package com.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.domain.Picture
import com.ui.databinding.PictureElementBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class RandomPicturesAdapter : ListAdapter<Picture, RandomPicturesAdapter.PictureViewHolder>(DiffCallback()) {

    private var onLikeClickListener: ((Picture) -> Unit)? = null
    private var localDirectoryPath: String? = null

    inner class PictureViewHolder(private val binding: PictureElementBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(picture: Picture) {
            Glide.with(binding.pictureImageView.context)
                .load(picture.download_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.pictureImageView)

            binding.likeButton.setOnClickListener {
                try {
                    localDirectoryPath?.let { path ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val file = Glide.with(binding.pictureImageView.context)
                                .downloadOnly()
                                .load(picture.download_url)
                                .submit()
                                .get()

                            val destinationFile = File(path, "${picture.id}.png")
                            file.copyTo(destinationFile, overwrite = true)

                            withContext(Dispatchers.Main) {
                                onLikeClickListener?.invoke(picture.copy(localPath = destinationFile.absolutePath))
                            }
                            Log.d("RandomPicturesAdapter", "${picture.id} - Image saved")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("RandomPicturesAdapter", "Error saving image: ${e.message}", e)
                }
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

    fun setLocalDirectoryPath(path: String?) {
        localDirectoryPath = path
    }

    fun setOnLikeClickListener(listener: (Picture) -> Unit) {
        onLikeClickListener = listener
    }

    private class DiffCallback : DiffUtil.ItemCallback<Picture>() {
        override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean =
            oldItem == newItem
    }
}
