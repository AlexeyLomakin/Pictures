package com.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ui.databinding.LikedPicturesFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LikedPicturesFragment : Fragment(R.layout.liked_pictures_fragment) {

    private val binding by viewBinding(LikedPicturesFragmentBinding::bind)
    private val viewModel: LikedPicturesViewModel by viewModels()
    private val adapter = LikedPicturesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.likedPicturesRv.adapter = adapter
        binding.likedPicturesRv.layoutManager = LinearLayoutManager(context)

        observeFavoritePictures()
        setupUnlikeButton()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavoritePictures()
    }

    private fun observeFavoritePictures() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoritePicturesState.collect { pictures ->
                adapter.submitList(pictures)
            }
        }
    }

    private fun setupUnlikeButton() {
        adapter.setOnUnlikeClickListener { picture ->
            viewModel.unlikePicture(picture)
        }
    }
}
