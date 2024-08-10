package com.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.LoadFavoritePicturesUseCase
import com.domain.Picture
import com.domain.UnlikePictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedPicturesViewModel @Inject constructor(
    private val loadFavoritePicturesUseCase: LoadFavoritePicturesUseCase,
    private val unlikePictureUseCase: UnlikePictureUseCase
) : ViewModel() {

    private val _favoritePicturesState = MutableStateFlow<List<Picture>>(emptyList())
    val favoritePicturesState: StateFlow<List<Picture>> get() = _favoritePicturesState

    init {
        loadFavoritePictures()
    }


    private fun loadFavoritePictures() {
        viewModelScope.launch {
            loadFavoritePicturesUseCase.execute()
                .collect { pictures ->
                    _favoritePicturesState.value = pictures
                }
        }
    }

    fun unlikePicture(picture: Picture) {
        viewModelScope.launch {
            unlikePictureUseCase.execute(picture)
            loadFavoritePictures()
        }
    }
}