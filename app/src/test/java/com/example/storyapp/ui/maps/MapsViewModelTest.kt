package com.example.storyapp.ui.maps

import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var locationViewModel: MapsViewModel

    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyToken = "AUTH_TOKEN"

    @Before
    fun setup() {
        locationViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `Get story with location successfully`(): Unit = runTest {

        val expectedResponse = flowOf(Result.success(dummyStoriesResponse))

        Mockito.`when`(locationViewModel.getStoryLocation(dummyToken)).thenReturn(expectedResponse)

        locationViewModel.getStoryLocation(dummyToken).collect { actualResponse ->

            assertTrue(actualResponse.isSuccess)
            assertFalse(actualResponse.isFailure)

            actualResponse.onSuccess { storiesResponse ->
                assertNotNull(storiesResponse)
                assertSame(storiesResponse, dummyStoriesResponse)
            }
        }

        Mockito.verify(storyRepository).getAllStoriesWithLocation(dummyToken)
    }

    @Test
    fun `Get story with location failed`(): Unit = runTest {

        val expectedResponse: Flow<Result<StoriesResponse>> =
            flowOf(Result.failure(Exception("Failed")))

        Mockito.`when`(locationViewModel.getStoryLocation(dummyToken)).thenReturn(expectedResponse)

        locationViewModel.getStoryLocation(dummyToken).collect { actualResponse ->

            assertFalse(actualResponse.isSuccess)
            assertTrue(actualResponse.isFailure)

            actualResponse.onFailure {
                assertNotNull(it)
            }
        }

        Mockito.verify(storyRepository).getAllStoriesWithLocation(dummyToken)
    }
}