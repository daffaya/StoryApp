package com.example.storyapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.storyapp.data.Story
import com.example.storyapp.data.response.StoryResponseItem

class DiffUtilCallback(
    private val old: List<StoryResponseItem>,
    private val new: List<StoryResponseItem>,
    private val listener: DiffCallbackListener<StoryResponseItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        listener.areItemsTheSame(old[oldItemPosition], new[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition] == new[newItemPosition]
}

interface DiffCallbackListener<T> {
    fun areItemsTheSame(oldItem: T, newItem: T): Boolean
}