package com.example.storyapp.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SettingViewModel constructor(
    private val authRepository: AuthRepository
    ): ViewModel() {
    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }
}
