package com.example.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.databinding.FragmentHomeBinding
import com.example.storyapp.ui.story.AddStoryActivity
import com.example.storyapp.utils.DiffCallbackListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter(diffCallbackListener) }

    companion object {
        private val diffCallbackListener = object : DiffCallbackListener<StoryResponseItem> {
            override fun areItemsTheSame(
                oldItem: StoryResponseItem,
                newItem: StoryResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))

        swipeRefresh()
        showListStory()

        return binding.root
    }

    private fun showListStory() {
        if (binding.rvStory.adapter == null) {
            binding.rvStory.adapter = homeAdapter
        }

        homeViewModel.getStories()
        lifecycleScope.launch {
            homeViewModel.storyList.collect { storyList ->
                Log.d(HomeFragment::class.simpleName, "List: $storyList")
                homeAdapter.setItems(storyList)
                binding.swipeRefresh.isRefreshing = false
                binding.rvStory.scrollToPosition(0)
            }
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStory.adapter = homeAdapter


        binding.fabAddStory.setOnClickListener {
            Intent(requireContext(), AddStoryActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }


    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            showListStory()
        }
    }
}
