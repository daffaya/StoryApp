package com.example.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.storyapp.data.Story
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.detail.DetailStoryActivity
import com.example.storyapp.utils.DiffCallbackListener
import com.example.storyapp.utils.DiffUtilCallback

class HomeAdapter(
    private val storyList: List<Story>,
    private val diffUtilCallbackListener: DiffCallbackListener<Story>
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var _items = mutableListOf<Story>()

    fun setItems(items: List<Story>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(_items, items.toMutableList(), diffUtilCallbackListener))
        _items = items.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    binding.root.context as Activity,
                    Pair(binding.ivItemPhoto, "photo"),
                    Pair(binding.tvItemName, "name"),
                    Pair(binding.tvItemDesc, "description"),
                )

                binding.root.context.startActivity(
                    Intent(binding.root.context, DetailStoryActivity::class.java)
                        .putExtra(DetailStoryActivity.EXTRA_DETAIL, itemId),
                    optionsCompat.toBundle()
                )
            }
        }

        fun bind(item: Story) {
            binding.tvItemName.text = item.name
            binding.tvItemDesc.text = item.description
            Glide.with(binding.root.context).load(item.photoUrl).into(binding.ivItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    private fun getItem(position: Int): Story {
        return storyList[position]
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

