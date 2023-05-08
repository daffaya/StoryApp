package com.example.storyapp.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.data.Story
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.detail.DetailStoryActivity
import com.example.storyapp.ui.detail.DetailStoryActivity.Companion.EXTRA_DETAIL
import com.example.storyapp.utils.dateFormat
import com.example.storyapp.utils.setImageFromUrl

class StoryListAdapter(private val storyList: List<Story>) :
    RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: Story) {
            binding.apply {
                tvItemName.text = story.name
                tvItemDesc.text = story.description
                ivItemPhoto.setImageFromUrl(context, story.photoUrl)
                tvItemDate.dateFormat(story.createdAt)

                // On item clicked
                root.setOnClickListener {
                    // Set ActivityOptionsCompat for SharedElement
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(ivItemPhoto, "story_image"),
                            Pair(tvItemName, "username"),
                            Pair(tvItemDate, "date"),
                            Pair(tvItemDesc, "description")
                        )

                    Intent(context, DetailStoryActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_DETAIL, story)
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(holder.itemView.context, story)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }
}