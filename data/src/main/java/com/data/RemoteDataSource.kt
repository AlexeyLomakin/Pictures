package com.data

import com.domain.Picture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val picturesService: PicturesService
) {

    fun fetchRandomPictures(page: Int, limit: Int): Flow<List<Picture>> = flow {
        val pictures = picturesService.getPictures(page, limit)
        emit(pictures.map { picture ->
            Picture(
                id = picture.id,
                download_url = picture.download_url
            )
        })
    }
}

