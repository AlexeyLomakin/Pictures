package com.data

import com.domain.Picture
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.io.File

class PicturesRepositoryImplTest {

    private lateinit var repository: PicturesRepositoryImpl
    private val remoteDataSource: RemoteDataSource = mock(RemoteDataSource::class.java)
    private val localDataSource: LocalDataSource = mock(LocalDataSource::class.java)

    @Before
    fun setUp() {
        repository = PicturesRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `fetchRandomPictures should return pictures from remote data source`() = runTest {
        val fakePictures = listOf(Picture(id = "1", download_url = "http://example.com/1.png"))
        `when`(remoteDataSource.fetchRandomPictures(1, 10)).thenReturn(flowOf(Result.success(fakePictures)))

        val result = repository.fetchRandomPictures(1, 10).first()

        assertEquals(Result.success(fakePictures), result)
        verify(remoteDataSource).fetchRandomPictures(1, 10)
    }

    @Test
    fun `getFavoritePictures should return pictures from local data source`() = runTest {
        val directoryPath = "/path/to/directory"
        `when`(localDataSource.getLocalDirectoryPath()).thenReturn(directoryPath)

        val files = arrayOf(File("$directoryPath/1.png"), File("$directoryPath/2.png"))
        `when`(File(directoryPath).listFiles()).thenReturn(files)

        val expectedPictures = listOf(
            Picture(id = "1", download_url = "", localPath = "$directoryPath/1.png"),
            Picture(id = "2", download_url = "", localPath = "$directoryPath/2.png")
        )

        val result = repository.getFavoritePictures().first()

        assertEquals(expectedPictures, result)
        verify(localDataSource).getLocalDirectoryPath()
    }

    @Test
    fun `likePicture should add picture to favorites`() = runTest {
        val picture = Picture(id = "1", download_url = "http://example.com/1.png")

        repository.likePicture(picture)

        assertTrue(repository.getFavoritePictures().first().contains(picture))
    }

    @Test
    fun `unlikePicture should remove picture from favorites and delete image`() = runTest {
        val picture = Picture(id = "1", download_url = "", localPath = "/path/to/file.png")
        repository.likePicture(picture)

        repository.unlikePicture(picture)

        assertFalse(repository.getFavoritePictures().first().contains(picture))
        verify(localDataSource).getOrCreateLocalDirectory()
    }

    @Test
    fun `getLocalDirectoryPath should return local directory path from local data source`() {
        val expectedPath = "/path/to/directory"
        `when`(localDataSource.getLocalDirectoryPath()).thenReturn(expectedPath)

        val result = repository.getLocalDirectoryPath()

        assertEquals(expectedPath, result)
        verify(localDataSource).getLocalDirectoryPath()
    }
}
