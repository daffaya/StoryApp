package com.example.storyapp.data.repository

import com.example.storyapp.data.Story
import com.example.storyapp.data.response.FileUploadResponse
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import kotlin.collections.map

class StoryRepository constructor(
    private val apiService: ApiService
) {

    private var client: ApiService = ApiConfig().getApiService()

    suspend fun getStory(token: String) = client.getAllStories(token)
    suspend fun uploadImage(
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

//    abstract fun getStories(): Flow<StoriesResponse>

    suspend fun getAllStories(token: String, response: Response<StoriesResponse>): List<Story> {
        return try {
            if (response.isSuccessful) {
                val storiesResponse = response.body()
                val storyResponseItems: List<StoryResponseItem> = storiesResponse?.storyResponseItems ?: emptyList()

                // Convert StoryResponseItem to Story
                val stories = storyResponseItems.map { storyResponseItem: StoryResponseItem ->
                    Story(
                        id = storyResponseItem.id,
                        name = storyResponseItem.name,
                        description = storyResponseItem.description,
                        createdAt = storyResponseItem.createdAt,
                        photoUrl = storyResponseItem.photoUrl
                    )
                }

                stories
            } else {
                // Handle error response
                emptyList()
            }
        } catch (e: Exception) {
            // Handle network error
            emptyList()
        }
    }



    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }

}