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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        const val TAG = "HomeViewModel"
    }

    val repository: StoryRepository = StoryRepository(apiService)

    fun getStories() {
        viewModelScope.launch {
            authPreferencesDataStore.getToken().collect { token ->
                token?.let {
                    val retrievedTodo = repository.getStory("Bearer $it")

                    _storyList.value = retrievedTodo.storyResponseItems
                }
            }
        }
    }

    val getStory1 = liveData(Dispatchers.IO) {
        authPreferencesDataStore.getToken().collect { token ->
            token?.let {
                val retrievedTodo = repository.getStory(it)
                emit(retrievedTodo)
            }
        }

    }
//    suspend fun listStory(token: String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().getAllStories(token)
//        client.enqueue(object : Callback<StoriesResponse> {
//            override fun onResponse(
//                call: Call<StoriesResponse>,
//                response: Response<StoriesResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _storyList.value = response.body()
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(TAG, "onFailure: ${t.message.toString()}")
//            }
//        })
//    }
}