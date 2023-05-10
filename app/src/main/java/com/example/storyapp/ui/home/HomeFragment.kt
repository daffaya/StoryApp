package com.example.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.MainActivity
import com.example.storyapp.data.Story
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.response.StoryResponseItem
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.databinding.FragmentHomeBinding
import com.example.storyapp.ui.story.AddStoryActivity
import com.example.storyapp.utils.DiffCallbackListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    //    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
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

        showListStory()

        return binding.root
    }

    private fun showListStory() {
        homeViewModel.getStories()
        lifecycleScope.launch {
            homeViewModel.storyList.collect {
                Log.d(HomeFragment::class.simpleName, "List: $it")
                homeAdapter.setItems(it)
                binding.rvStory.adapter = homeAdapter
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvStory.adapter = homeAdapter

//        val apiConfig = ApiConfig()
//        val apiService = apiConfig.getApiService()

//        val storyRepository = ConcreteStoryRepository(apiService)
//        val homeGetStory = HomeGetStory(storyRepository)
//        homeViewModel = HomeFragmentViewModel(homeGetStory, storyRepository)
//
//        homeViewModel = HomeFragmentViewModel(
//            HomeGetStory(storyRepository),
//            ConcreteStoryRepository(apiService) // Provide the necessary implementation for StoryRepository
//        )
//
//        homeViewModel.storyResult.launchAndCollectIn(this) {
//            when (it.resultStories) {
//                is ResultState.Success -> {
//                    binding.rvLoading.visibility = View.GONE
//                    it.resultStories.data?.let { stories ->
//                        homeAdapter.setItems(stories) // Call setItems on the adapter instance
//                    }
//                }
//                is ResultState.Error -> {
//                    binding.rvLoading.visibility = View.GONE
//                }
//                is ResultState.Loading -> {
//                    binding.rvLoading.visibility = View.VISIBLE
//                }
//                else -> Unit
//            }
//        }

        binding.fabAddStory.setOnClickListener {
            Intent(requireContext(), AddStoryActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }
}
