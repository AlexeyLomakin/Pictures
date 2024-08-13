package com.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class LikedPicturesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val loadFavoritePicturesUseCase: LoadFavoritePicturesUseCase = mock(LoadFavoritePicturesUseCase::class.java)
    private val unlikePictureUseCase: UnlikePictureUseCase = mock(UnlikePictureUseCase::class.java)

    private lateinit var viewModel: LikedPicturesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = LikedPicturesViewModel(loadFavoritePicturesUseCase, unlikePictureUseCase)
    }

    @Test
    fun `loadFavoritePictures should update favoritePicturesState`() = runTest {
        val favoritePictures = listOf(Picture(id = "1", "url",localPath = "/path/to/file"))
        `when`(loadFavoritePicturesUseCase.execute()).thenReturn(flowOf(favoritePictures))

        viewModel.loadFavoritePictures()

        val result = viewModel.favoritePicturesState.value
        assertEquals(favoritePictures, result)
    }

    @Test
    fun `unlikePicture should call unlikePictureUseCase`() = runTest {
        val picture = Picture(id = "1","url", localPath = "/path/to/file")

        viewModel.unlikePicture(picture)

        verify(unlikePictureUseCase).invoke(picture)
    }
}
