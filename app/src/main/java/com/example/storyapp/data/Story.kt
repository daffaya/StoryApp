package com.example.storyapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Parcelize
data class Story(
    val id: String,
    val name: String,
    val description: String,
    val createdAt: String,
    val photoUrl: String,
) : Parcelable