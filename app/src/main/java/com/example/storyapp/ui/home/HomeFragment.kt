package com.example.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.MainActivity
import com.example.storyapp.data.Story
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.ResultState
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.databinding.FragmentHomeBinding
import com.example.storyapp.ui.story.AddStoryActivity
import com.example.storyapp.utils.DiffCallbackListener
import com.example.storyapp.utils.launchAndCollectIn

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
//    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private var token: String = MainActivity.EXTRA_TOKEN
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var client: ApiService = ApiConfig().getApiService()


    private val homeAdapter: HomeAdapter by lazy { HomeAdapter(emptyList(), diffCallbackListener) }

    companion object {
        private val diffCallbackListener = object : DiffCallbackListener<Story> {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding.root

        showListStory()
    }

    private fun showListStory() {
        homeViewModel.getStory1.observe(viewLifecycleOwner, Observer {
            binding.rvStory.adapter = homeAdapter

            return@Observer
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = requireActivity().intent.getStringExtra(MainActivity.EXTRA_TOKEN) ?: ""

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
