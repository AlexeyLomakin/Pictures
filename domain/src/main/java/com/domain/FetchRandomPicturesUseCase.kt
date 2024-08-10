package com.domain


import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchRandomPicturesUseCase @Inject constructor(
    private val repository: PicturesRepository
) {
    fun execute(page: Int, limit: Int): Flow<List<Picture>> {
        return repository.fetchRandomPictures(page, limit)
    }
}
