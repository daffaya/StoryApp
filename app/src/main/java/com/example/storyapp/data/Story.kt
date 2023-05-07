package com.example.storyapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Parcelize
@Entity(tableName = "story")
data class Story(
    @PrimaryKey
    val id: String,

    val name: String,

    val description: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String,

) : Parcelable