package com.example.storyapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storyapp.data.local.RemoteKeysDao
import com.example.storyapp.data.local.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}