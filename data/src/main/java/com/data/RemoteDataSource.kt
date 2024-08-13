package com.data

import com.domain.Picture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val picturesService: PicturesService
) {

    fun fetchRandomPictures(page: Int, limit: Int): Flow<Result<List<Picture>>> = flow {
        try {
            val response = picturesService.getPictures(page, limit)
            if (response.isSuccessful) {
                val pictures = response.body() ?: emptyList()
                emit(Result.success(pictures.map { picture ->
                    Picture(
                        id = picture.id,
                        download_url = picture.download_url
                    )
                }))
            } else {
                emit(Result.failure(HttpException(response)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}



