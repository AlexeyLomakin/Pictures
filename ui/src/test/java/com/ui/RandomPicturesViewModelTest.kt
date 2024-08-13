package com.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class RandomPicturesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val fetchRandomPicturesUseCase: FetchRandomPicturesUseCase = mock(FetchRandomPicturesUseCase::class.java)
    private val likePictureUseCase: LikePictureUseCase = mock(LikePictureUseCase::class.java)
    private val getLocalDirectoryPathUseCase: GetLocalDirectoryPathUseCase = mock(GetLocalDirectoryPathUseCase::class.java)

    private lateinit var viewModel: RandomPicturesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = RandomPicturesViewModel(fetchRandomPicturesUseCase, likePictureUseCase, getLocalDirectoryPathUseCase)
    }

    @Test
    fun `fetchPictures should update pictures state`() = runTest {
        val fakePictures = listOf(Picture(id = "1", download_url = "http://example.com/1.png"))
        `when`(fetchRandomPicturesUseCase.execute(1, 10)).thenReturn(flowOf(Result.success(fakePictures)))

        viewModel.fetchPictures()

        val result = viewModel.pictures.value
        assertEquals(fakePictures, result)
    }

    @Test
    fun `likePicture should call likePictureUseCase`() = runTest {
        val picture = Picture(id = "1", download_url = "http://example.com/1.png")

        viewModel.likePicture(picture)

        verify(likePictureUseCase).invoke(picture)
    }
}
