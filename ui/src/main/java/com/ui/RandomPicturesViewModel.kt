package com.ui


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.FetchRandomPicturesUseCase
import com.domain.GetLocalDirectoryPathUseCase
import com.domain.LikePictureUseCase
import com.domain.Picture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomPicturesViewModel @Inject constructor(
    private val fetchRandomPicturesUseCase: FetchRandomPicturesUseCase,
    private val likePictureUseCase: LikePictureUseCase,
    getLocalDirectoryPathUseCase: GetLocalDirectoryPathUseCase
) : ViewModel() {

    private val _pictures = MutableStateFlow<List<Picture>>(emptyList())
    val pictures: StateFlow<List<Picture>> = _pictures

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _localDirectoryPath = MutableStateFlow<String?>(null)
    val localDirectoryPath: StateFlow<String?> = _localDirectoryPath

    private var currentPage = 1
    private val limit = 20

    init {
        fetchPictures()
        _localDirectoryPath.value = getLocalDirectoryPathUseCase.execute()
    }

    fun fetchPictures() {
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            fetchRandomPicturesUseCase.execute(currentPage, limit).collect { newPictures ->
                _pictures.update { currentPictures ->
                    currentPictures + newPictures
                }
                _isLoading.value = false
                currentPage++
            }
        }
    }

    fun likePicture(picture: Picture) {
        viewModelScope.launch {
            val localPath = _localDirectoryPath.value
            if (localPath != null) {
                likePictureUseCase.invoke(picture.copy(localPath = localPath))
            } else {
                Log.e("RandomPicturesViewModel", "Local directory path is null")
            }
        }
    }
}

