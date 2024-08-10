package com.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.FetchRandomPicturesUseCase
import com.domain.LikePictureUseCase
import com.domain.Picture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RandomPicturesViewModel @Inject constructor(
    private val fetchRandomPicturesUseCase: FetchRandomPicturesUseCase,
    private val likePictureUseCase: LikePictureUseCase
) : ViewModel() {

    private val _picturesState = MutableStateFlow<List<Picture>>(emptyList())
    val picturesState: StateFlow<List<Picture>> get() = _picturesState

    private var currentPage = 1
    private val pageSize = 20

    init {
        fetchPictures()
    }

    private fun fetchPictures() {
        viewModelScope.launch {
            fetchRandomPicturesUseCase.execute(page = currentPage, limit = pageSize)
                .collect { pictures ->
                    _picturesState.value += pictures
                    currentPage++
                }
        }
    }

    fun loadNextPage() {
        fetchPictures()
    }

    fun likePicture(picture: Picture) {
        viewModelScope.launch {
            likePictureUseCase.execute(picture)
        }
    }


}