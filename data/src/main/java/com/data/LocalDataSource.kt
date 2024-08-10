package com.data

import com.domain.Picture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface LocalDataSource {
    fun getFavoritePictures(): Flow<List<Picture>>
    suspend fun savePicture(picture: Picture)
    suspend fun deletePicture(picture: Picture)
}

class LocalDataSourceImpl: LocalDataSource {

    private val favoritePictures = mutableSetOf<Picture>()

    override fun getFavoritePictures(): Flow<List<Picture>> = flow {
        emit(favoritePictures.toList())
    }

    override suspend fun savePicture(picture: Picture) {
        favoritePictures.add(picture)
    }

    override suspend fun deletePicture(picture: Picture) {
        favoritePictures.remove(picture)
    }
}