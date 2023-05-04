package com.example.storyapp.data.repository

import com.example.storyapp.data.response.GeneralResponse
import com.example.storyapp.data.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(email: String, password: String, name: String): Flow<GeneralResponse>
    fun login(email: String, password: String): Flow<LoginResponse>
}