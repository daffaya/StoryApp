package com.example.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.StoriesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MapsViewModel @Inject constructor(
    private val authPreferencesDataStore: AuthPreferencesDataStore,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val resultLiveData = MutableLiveData<Result<StoriesResponse>>()

    fun getStoryLocations(): LiveData<Result<StoriesResponse>> {
        viewModelScope.launch {
            try {
                val token = withContext(Dispatchers.IO) { authPreferencesDataStore.getToken().firstOrNull() }
                if (token != null) {
                    withContext(Dispatchers.IO) {
                        storyRepository.getAllStoriesWithLocation(token).collect { result ->
                            resultLiveData.postValue(result)
                        }
                    }
                } else {
                    resultLiveData.postValue(Result.failure(Exception("Token not available")))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                resultLiveData.postValue(Result.failure(e))
            }
        }
        return resultLiveData
    }
}

