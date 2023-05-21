package com.example.storyapp.ui.story

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.data.repository.AuthRepository
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.FileUploadResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class AddStoryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
): ViewModel() {
    fun getAuthToken(): Flow<String?> = authRepository.getAuthToken()
    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Result<FileUploadResponse>> =
        storyRepository.uploadStory(token, file, description, lat, lon)

}