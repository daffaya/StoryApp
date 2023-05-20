package com.example.storyapp.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.local.Story

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun uploadStory(vararg story: Story)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, Story>


    @Query("DELETE FROM story")
    fun deleteAll()
}