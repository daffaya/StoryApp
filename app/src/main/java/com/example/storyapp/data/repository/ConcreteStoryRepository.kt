package com.example.storyapp.data.repository

import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

//class ConcreteStoryRepository(private val apiService: ApiService) : StoryRepository(apiService) {
//    override fun getStories(): Flow<StoriesResponse> = flow {
//        emit(
//            apiService.getAllStories(token = "")
//        )
//    }.flowOn(Dispatchers.IO)
//}
