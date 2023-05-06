package com.example.storyapp.data.retrofit

import com.example.storyapp.data.response.GeneralResponse
import com.example.storyapp.data.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body requestBody: HashMap<String, String>
    ): LoginResponse

    @POST("login")
    suspend fun login(
        @Body requestBody: HashMap<String, String>
    ): retrofit2.Response<LoginResponse>

}