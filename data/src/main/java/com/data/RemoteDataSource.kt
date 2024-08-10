package com.data

import com.domain.Picture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSource(private val picturesService: PicturesService) {

    suspend fun fetchRandomPictures(page: Int, limit: Int): List<Picture> = withContext(Dispatchers.IO) {
        val response = picturesService.getPictures(page, limit)
        response.map { picture ->
            Picture(
                id = picture.id,
                url = picture.url,
                download_url = picture.download_url
            )
        }
    }
}
