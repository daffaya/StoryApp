package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.repository.AuthRepository
import com.example.storyapp.data.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LoginViewModel constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> =
        authRepository.login(email, password)

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }
}
