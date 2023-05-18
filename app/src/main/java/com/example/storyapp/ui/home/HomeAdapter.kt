package com.example.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.detail.DetailStoryActivity
import com.example.storyapp.utils.DiffCallbackListener
import com.example.storyapp.utils.DiffUtilCallback

class HomeAdapter(
    private val diffUtilCallbackListener: DiffCallbackListener<StoryResponseItem>
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var _items = mutableListOf<StoryResponseItem>()

    fun setItems(items: List<StoryResponseItem>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffUtilCallback(_items, items.toMutableList(), diffUtilCallbackListener)
        )
        _items.clear()
        _items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: StoryResponseItem) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    private fun getItem(position: Int): StoryResponseItem {
        return _items[position]
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

