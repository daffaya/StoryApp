package com.example.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.AuthRepository
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.FileUploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
): ViewModel() {
    fun getAuthToken(): Flow<String?> = authRepository.getAuthToken()
    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
    ): Flow<Result<FileUploadResponse>> =
        storyRepository.uploadImage(token, file, description)
}