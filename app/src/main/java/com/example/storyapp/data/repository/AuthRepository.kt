package com.example.storyapp.data.repository

import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository constructor(
    private val apiService: ApiService,
    private val preferencesDataStore: AuthPreferencesDataStore
){
    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.login(email, password)
            val loginResponse = response.body()
            if (response.isSuccessful && loginResponse != null) {
                emit(Result.success(loginResponse))
            } else {
                emit(Result.failure(Exception("Login failed")))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.register(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveAuthToken(token: String) {
        preferencesDataStore.saveToken(token)
    }

    suspend fun clearToken(token: String){
        preferencesDataStore.clearToken(token)
    }

    fun getAuthToken(): Flow<String?> = preferencesDataStore.getToken()
}