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

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var viewModel: DetailStoryViewModel

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Story>(EXTRA_DETAIL)
        parseStoryData(story)
    }

    private fun parseStoryData(story: Story?) {
        if (story != null) {
            binding.apply {
                tvStoryUsername.text = story.name
                tvStoryDescription.text = story.description
                tvStoryDate.dateFormat(story.createdAt)

//                Glide
//                    .with(this@DetailStoryActivity)
//                    .load(story.photoUrl)
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(
//                            e: GlideException?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            // Continue enter animation after image loaded
//                            supportStartPostponedEnterTransition()
//                            return false
//                        }
//
//                        override fun onResourceReady(
//                            resource: Drawable?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            dataSource: DataSource?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            supportStartPostponedEnterTransition()
//                            return false
//                        }
//                    })
//                    .placeholder(R.drawable.image_loading_placeholder)
//                    .error(R.drawable.image_load_error)
//                    .into(ivStoryImage)
            }
        }
    }

}