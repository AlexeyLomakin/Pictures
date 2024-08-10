package com.domain

import javax.inject.Inject

class UnlikePictureUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
    suspend operator fun invoke(picture: Picture) {
        repository.unlikePicture(picture)
    }
}
