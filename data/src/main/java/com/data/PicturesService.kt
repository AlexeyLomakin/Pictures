package com.data

import com.domain.Picture
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PicturesService {

    @GET("list")
    suspend fun getPictures(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
    ): Response<List<Picture>>
}