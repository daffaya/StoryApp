package com.example.storyapp.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.repository.AuthRepository
import com.example.storyapp.data.repository.StoryRepository

class AddStoryViewModelFactory(
    private val authRepository: AuthRepository,
    val storyRepository: StoryRepository
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(authRepository, storyRepository) as T
        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}
