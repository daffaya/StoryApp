package com.example.storyapp.ui.home

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.storyapp.MainActivity
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(dataStore: DataStore<Preferences>, apiService: ApiService): ViewModel() {
    private val _storyList = MutableLiveData<StoriesResponse>()
    val detailUser: LiveData<StoriesResponse> = _storyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private var token: String = MainActivity.EXTRA_TOKEN

    companion object{
        const val TAG = "HomeViewModel"
    }

    val repository: StoryRepository = StoryRepository(apiService)

    val getStory1 = liveData(Dispatchers.IO) {
        val retrivedTodo = repository.getStory(token)

        emit(retrivedTodo)
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