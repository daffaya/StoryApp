package com.example.storyapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

    @Provides
    @Singleton
    fun provideAuthPreferencesDataStore(app: Application): AuthPreferencesDataStore {
        return AuthPreferencesDataStore(app.dataStore)
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiConfig().getApiService()
    }

}