package com.domain

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoadFavoritePicturesUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
    fun execute(): Flow<List<Picture>> {
        return repository.getFavoritePictures()
    }
}
