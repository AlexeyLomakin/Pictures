package com.domain

import kotlinx.coroutines.flow.Flow

interface PicturesRepository {
    suspend fun fetchRandomPictures(page: Int, limit: Int): Flow<Result<List<Picture>>>
    suspend fun getFavoritePictures(): Flow<List<Picture>>
    suspend fun likePicture(picture: Picture)
    suspend fun unlikePicture(picture: Picture)
    fun getLocalDirectoryPath(): String?
}