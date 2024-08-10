package com.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchRandomPicturesUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
    suspend fun execute(page: Int, limit: Int): Flow<Result<List<Picture>>> {
        return repository.fetchRandomPictures(page, limit)
    }
}
