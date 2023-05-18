package com.example.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    suspend fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)
}
