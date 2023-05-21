package com.example.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapp.data.local.Story
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.remote.StoryRemoteMediator
import com.example.storyapp.data.response.FileUploadResponse
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {
    fun getAllStories(token: String): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                apiService,
                token
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).flow
    }
    fun getAllStoriesWithLocation(token: String): Flow<Result<StoriesResponse>> = flow {
        try {
            val bearerToken = generateBearerToken(token)
            val response = apiService.getAllStories(bearerToken, size = 30, location = 1)
            emit(Result.success<StoriesResponse>(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure<StoriesResponse>(e))
        }
    }




    suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Flow<Result<FileUploadResponse>> = flow {
        try {
            val bearerToken = generateBearerToken(token)
            val response = apiService.uploadImage(bearerToken, file, description, lat, lon)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }
}