package com.example.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.data.local.Story
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.StoryPagingSource
import com.example.storyapp.utils.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = MainDispatcherRule()

    @Mock
    private lateinit var authPreferencesDataStore: AuthPreferencesDataStore

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var homeViewModel: HomeViewModel

    private val dummyToken = "authentication_token"

    @Before
    fun setup() {
        homeViewModel = HomeViewModel(authPreferencesDataStore, storyRepository)
    }

    @Test
    fun `Get all stories successfully`() = runTest {
        val dummyStories = DataDummy.generateDummyListStory()
        val data = StoryPagingSource.snapshot(dummyStories)
        val flow = flowOf(data)

        `when`(authPreferencesDataStore.getToken()).thenReturn(flowOf(dummyToken))
        `when`(storyRepository.getAllStories("Bearer $dummyToken")).thenReturn(flow)

        val actualStoriesLiveData: LiveData<PagingData<Story>> = homeViewModel.getStoryList()
        val actualStories = actualStoriesLiveData.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        verify(authPreferencesDataStore).getToken()
        verify(storyRepository).getAllStories("Bearer $dummyToken")
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}



