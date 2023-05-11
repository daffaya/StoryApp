package com.example.storyapp.ui.detail

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.storyapp.R
import com.example.storyapp.data.Story
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.utils.dateFormat
import javax.sql.DataSource
import com.bumptech.glide.request.target.Target
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.data.response.StoryResponseItem

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var viewModel: DetailStoryViewModel

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<StoryResponseItem>(EXTRA_DETAIL)
        getStoryDetail(story)
    }

    private fun getStoryDetail(story: StoryResponseItem?) {
        if (story != null) {
            binding.apply {
                tvStoryUsername.text = story.name
                tvStoryDescription.text = story.description
                tvStoryDate.dateFormat(story.createdAt)

                Glide
                    .with(this@DetailStoryActivity)
                    .load(story.photoUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    })
                    .placeholder(R.drawable.ic_image_24)
                    .error(R.drawable.ic_broken_image_24)
                    .into(ivStoryPhoto)
            }
        }
    }
}
