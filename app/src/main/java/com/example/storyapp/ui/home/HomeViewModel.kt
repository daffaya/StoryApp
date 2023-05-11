package com.example.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.data.retrofit.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authPreferencesDataStore: AuthPreferencesDataStore,
    private val apiService: ApiService,
) : ViewModel() {
    private val _storyList = MutableStateFlow<List<StoryResponseItem>>(emptyList())
    val storyList: StateFlow<List<StoryResponseItem>> = _storyList


    val repository: StoryRepository = StoryRepository(apiService)

    fun getStories() {
        viewModelScope.launch {
            authPreferencesDataStore.getToken().collect { token ->
                token?.let {
                    val retrieveStory = repository.getStory("Bearer $it")

                    _storyList.value = retrieveStory.storyResponseItems
                }
            }
        }
    }

}
