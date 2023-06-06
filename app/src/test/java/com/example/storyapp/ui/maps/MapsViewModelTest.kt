package com.example.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

@Suppress("DEPRECATION")
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = MainDispatcherRule() // Add this rule
    @Mock
    private lateinit var storyRepository: StoryRepository
    @Mock
    private lateinit var authPreferencesDataStore: AuthPreferencesDataStore
    private lateinit var mapsViewModel: MapsViewModel

    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyToken = "AUTH_TOKEN"

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mapsViewModel = MapsViewModel(authPreferencesDataStore, storyRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Get story with location successfully`() = testDispatcher.runBlockingTest {

        val expectedResponse = dummyStoriesResponse
        val tokenFlow = flowOf(dummyToken)

        Mockito.`when`(authPreferencesDataStore.getToken()).thenReturn(tokenFlow)
        Mockito.`when`(storyRepository.getAllStoriesWithLocation(dummyToken)).thenAnswer {
            val resultFlow = flowOf(Result.success(expectedResponse))
            resultFlow
        }

        val actualResponseLiveData = mapsViewModel.getStoryLocations()
        val actualResponse = actualResponseLiveData.getOrAwaitValue()

        assertTrue(actualResponse.isSuccess)
        assertFalse(actualResponse.isFailure)

        actualResponse.onSuccess { storiesResponse ->
            assertNotNull(storiesResponse)
            assertSame(storiesResponse, expectedResponse)
        }

        Mockito.verify(authPreferencesDataStore).getToken()
        Mockito.verify(storyRepository).getAllStoriesWithLocation(dummyToken)
    }

    @Test
    fun `Get story with location failed`() = testDispatcher.runBlockingTest {

        val expectedException = Exception("Failed")
        val tokenFlow = flowOf(dummyToken)

        Mockito.`when`(authPreferencesDataStore.getToken()).thenReturn(tokenFlow)
        Mockito.`when`(storyRepository.getAllStoriesWithLocation(dummyToken)).thenAnswer {
            val resultFlow = flowOf(Result.failure<StoriesResponse>(expectedException))
            resultFlow
        }

        val actualResponseLiveData = mapsViewModel.getStoryLocations()
        val actualResponse = actualResponseLiveData.getOrAwaitValue()

        assertFalse(actualResponse.isSuccess)
        assertTrue(actualResponse.isFailure)

        actualResponse.onFailure {
            assertEquals(expectedException, it)
        }

        Mockito.verify(authPreferencesDataStore).getToken()
        Mockito.verify(storyRepository).getAllStoriesWithLocation(dummyToken)
    }
}

