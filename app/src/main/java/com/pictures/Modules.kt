package com.pictures

import com.data.LocalDataSource
import com.data.LocalDataSourceImpl
import com.data.PicturesRepositoryImpl
import com.data.PicturesService
import com.data.RemoteDataSource
import com.domain.PicturesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    @Singleton
    fun providePicturesService(): PicturesService {
        val baseUrl = "https://picsum.photos/v2/"
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(PicturesService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        picturesService: PicturesService
    ): RemoteDataSource {
        return RemoteDataSource(picturesService)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(): LocalDataSource {
        return LocalDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): PicturesRepository {
        return PicturesRepositoryImpl(remoteDataSource, localDataSource)
    }
}
