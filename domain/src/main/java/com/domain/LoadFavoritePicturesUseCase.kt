package com.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadFavoritePicturesUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
   suspend fun execute(): Flow<List<Picture>> = repository.getFavoritePictures()
}

