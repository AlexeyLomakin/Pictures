package com.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ui.databinding.RandomPicturesFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomPicturesFragment : Fragment(R.layout.random_pictures_fragment) {

    private val binding by viewBinding(RandomPicturesFragmentBinding::bind)
    private val viewModel: RandomPicturesViewModel by viewModels()
    private val adapter = RandomPicturesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.addOnScrollListener(ArtsScrollListener())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.localDirectoryPath.collect { path ->
                adapter.setLocalDirectoryPath(path)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pictures.collect { pictures ->
                Log.d("RandomPicturesFragment", "Submitting ${pictures.size} pictures to the adapter")
                adapter.submitList(pictures)
            }
        }

        adapter.setOnLikeClickListener { picture ->
            viewModel.likePicture(picture)
        }
    }

    inner class ArtsScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                viewModel.fetchPictures()
            }
        }
    }
}
