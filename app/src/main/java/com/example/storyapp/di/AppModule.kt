package com.example.storyapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.storyapp.data.local.RemoteKeysDao
import com.example.storyapp.data.local.database.StoryDao
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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


    @Provides
    fun provideStoryDao(storyDatabase: StoryDatabase): StoryDao = storyDatabase.storyDao()

    @Provides
    fun provideRemoteKeysDao(storyDatabase: StoryDatabase): RemoteKeysDao =
        storyDatabase.remoteKeysDao()

    @Provides
    @Singleton
    fun provideStoryDatabase(@ApplicationContext context: Context): StoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StoryDatabase::class.java,
            "story_database"
        ).build()
    }
}