package com.example.storyapp.data.repository

import com.example.storyapp.data.retrofit.ApiService

class AuthRepository constructor(
    private val apiService: ApiService,
    private val preferencesDataStore: AuthPreferencesDataStore
){

}