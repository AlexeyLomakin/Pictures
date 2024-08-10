package com.data

import com.domain.PicturesRepository
import com.domain.Picture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PicturesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): PicturesRepository {

    override fun fetchRandomPictures(page: Int, limit: Int): Flow<List<Picture>> = flow {
        val pictures = remoteDataSource.fetchRandomPictures(page, limit)
        emit(pictures)
    }

    override fun getFavoritePictures(): Flow<List<Picture>> = localDataSource.getFavoritePictures()

    override suspend fun likePicture(picture: Picture) {
        localDataSource.savePicture(picture)
    }

    override suspend fun unlikePicture(picture: Picture) {
        localDataSource.deletePicture(picture)
    }
}
