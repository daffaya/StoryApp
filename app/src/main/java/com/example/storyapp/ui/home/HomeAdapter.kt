package com.example.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.local.Story
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.detail.DetailStoryActivity

class HomeAdapter : PagingDataAdapter<Story, HomeAdapter.ViewHolder>(DiffCallback)
{
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story) {
            binding.tvItemName.text = item.name
            binding.tvItemDesc.text = item.description
            Glide.with(binding.root.context).load(item.photoUrl).into(binding.ivItemPhoto)

            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    binding.root.context as Activity,
                    Pair(binding.ivItemPhoto, "photo"),
                    Pair(binding.tvItemName, "name"),
                    Pair(binding.tvItemDesc, "description"),
                )

                binding.root.context.startActivity(
                    Intent(binding.root.context, DetailStoryActivity::class.java)
                        .putExtra(DetailStoryActivity.EXTRA_DETAIL, item),
                    optionsCompat.toBundle()
                )
            }
        }
    }
}

