package com.domain

import javax.inject.Inject

class UnlikePictureUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
    suspend fun execute(picture: Picture) {
        repository.unlikePicture(picture)
    }
}