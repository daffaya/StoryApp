package com.example.storyapp.data.repository

import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesDataStore: AuthPreferencesDataStore
) {
    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.login(email, password)

            if (response.code() == 200) {
                response.body()?.let {
                    emit(Result.success<LoginResponse>(it))
                }
            } else {
                val errorBody = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    LoginResponse::class.java
                )

                emit(Result.failure<LoginResponse>(Throwable(errorBody.message)))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure<LoginResponse>(e))
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

    suspend fun clearToken(token: String) {
        preferencesDataStore.clearToken(token)
    }

    fun getAuthToken(): Flow<String?> = preferencesDataStore.getToken()

}