package com.domain

import javax.inject.Inject

class LikePictureUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
    suspend fun execute(picture: Picture) {
        repository.likePicture(picture)
    }
}