package com.example.storyapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailStoryViewModel @Inject constructor(
    private val authPreferencesDataStore: AuthPreferencesDataStore,
    private val apiService: ApiService
): ViewModel() {
    private val _detailStory = MutableStateFlow<List<StoryResponseItem>>(emptyList())
    val detailStory: StateFlow<List<StoryResponseItem>> = _detailStory

    val repository: StoryRepository = StoryRepository(apiService)
    fun getStories() {
        viewModelScope.launch {
            authPreferencesDataStore.getToken().collect { token ->
                token?.let {
                    val retrieveStoryDetail = repository.getStory("Bearer $it")

                    _detailStory.value = retrieveStoryDetail.storyResponseItems
                }
            }
        }
    }
}
