package com.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ui.PicturesAdapter
import com.ui.databinding.RandomPicturesFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomPicturesFragment : Fragment(R.layout.random_pictures_fragment) {

    private val binding by viewBinding(RandomPicturesFragmentBinding::bind)
    private val viewModel: RandomPicturesViewModel by viewModels()
    private val adapter = PicturesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observePictures()
    }

    private fun setupRecyclerView() {
        adapter.setOnLikeClickListener { picture ->
            viewModel.likePicture(picture)
        }

        binding.randomPicturesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.randomPicturesRv.adapter = adapter

        binding.randomPicturesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun observePictures() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.picturesState.collect { pictures ->
                adapter.submitList(pictures)
            }
        }
    }
}