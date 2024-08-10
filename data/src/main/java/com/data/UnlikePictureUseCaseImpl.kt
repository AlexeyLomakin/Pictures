package com.data

import com.domain.Picture
import com.domain.PicturesRepository
import javax.inject.Inject

class UnlikePictureUseCaseImpl @Inject constructor(
    private val repository: PicturesRepository
) {
    suspend fun execute(picture: Picture) {
        return repository.unlikePicture(picture)
    }
}