package com.example.storyapp.ui.maps

import androidx.lifecycle.*
import androidx.paging.*
import com.example.storyapp.data.local.Story
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.StoriesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MapsViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getStoryLocation(token: String): Flow<Result<StoriesResponse>> =
        storyRepository.getAllStoriesWithLocation(token)
}
