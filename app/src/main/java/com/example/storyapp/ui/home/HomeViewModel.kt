package com.example.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.local.Story
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authPreferencesDataStore: AuthPreferencesDataStore,
    private val storyRepository: StoryRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)

    val storyList: LiveData<PagingData<Story>> = authPreferencesDataStore.getToken()
        .flatMapLatest { token ->
            token?.let {
                storyRepository.getAllStories("Bearer $token")
                    .cachedIn(viewModelScope)
            } ?: flowOf(PagingData.empty())
        }.asLiveData()
}
