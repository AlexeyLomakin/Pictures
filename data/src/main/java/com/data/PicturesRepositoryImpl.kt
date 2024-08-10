package com.data

import com.domain.Picture
import com.domain.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class PicturesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : PicturesRepository {

    private val favoritePictures = mutableListOf<Picture>()

    override suspend fun fetchRandomPictures(page: Int, limit: Int): Flow<List<Picture>> =
        remoteDataSource.fetchRandomPictures(page, limit)

    override suspend fun getFavoritePictures(): Flow<List<Picture>> = flow {
        val directoryPath = localDataSource.getLocalDirectoryPath()
        val favoritePictures = mutableListOf<Picture>()

        directoryPath?.let { path ->
            val directory = File(path)
            val files = directory.listFiles { file -> file.extension == "png" }
            files?.forEach { file ->
                val pictureId = file.nameWithoutExtension.toLongOrNull()
                if (pictureId != null) {
                    favoritePictures.add(Picture(id = pictureId.toString(),"", localPath = file.absolutePath))
                }
            }
        }

        emit(favoritePictures)
    }

    override suspend fun likePicture(picture: Picture) {
        favoritePictures.add(picture)
    }

    override suspend fun unlikePicture(picture: Picture) {
        deleteImage(picture.id)
        favoritePictures.remove(picture)
    }

    private suspend fun deleteImage(imageName: String) = withContext(Dispatchers.IO) {
        val directory = localDataSource.getOrCreateLocalDirectory()
        val file = File(directory, "$imageName.png")
        if (file.exists()) {
            file.delete()
        }
    }
    override fun getLocalDirectoryPath(): String? {
        return localDataSource.getLocalDirectoryPath()
    }
}
