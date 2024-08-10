package com.domain

import javax.inject.Inject

class LikePictureUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
    suspend operator fun invoke(picture: Picture) {
        repository.likePicture(picture)
    }
}



