package com.domain

import kotlinx.coroutines.flow.Flow


interface PicturesRepository {
    fun fetchRandomPictures(page: Int, limit: Int): Flow<List<Picture>>
    fun getFavoritePictures(): Flow<List<Picture>>
    suspend fun likePicture(picture: Picture)
    suspend fun unlikePicture(picture: Picture)
}