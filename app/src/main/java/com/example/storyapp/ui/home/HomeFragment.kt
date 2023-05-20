package com.example.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.MainActivity
import com.example.storyapp.data.local.Story
import com.example.storyapp.databinding.FragmentHomeBinding
import com.example.storyapp.ui.story.AddStoryActivity
import com.example.storyapp.utils.animateVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var listAdapter: StoryListAdapter



    private lateinit var recyclerView: RecyclerView
    private var token: String = ""


    override fun onResume() {
        super.onResume()

        getAllStories()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvStory
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        token = requireActivity().intent.getStringExtra(MainActivity.EXTRA_TOKEN) ?: ""

        swipeRefresh()
        setRecyclerView()
        getAllStories()

        binding.fabAddStory.setOnClickListener {
            Intent(requireContext(), AddStoryActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }


    private fun getAllStories() {
        lifecycleScope.launch {
            homeViewModel.storyList.observe(viewLifecycleOwner) { pagingData ->
                updateRecyclerViewData(pagingData)
            }
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            getAllStories()
        }
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        listAdapter = StoryListAdapter()


        listAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && listAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {

                binding?.apply {
                    tvNotFoundError.animateVisibility(true)
                    ivNotFoundError.animateVisibility(true)
                    rvStory.animateVisibility(false)
                }
            } else {

                binding?.apply {
                    tvNotFoundError.animateVisibility(false)
                    ivNotFoundError.animateVisibility(false)
                    rvStory.animateVisibility(true)
                }
            }


            binding?.swipeRefresh?.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        try {
            recyclerView = binding.rvStory
            recyclerView.apply {
                adapter = listAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        listAdapter.retry()
                    }
                )
                layoutManager = linearLayoutManager
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun updateRecyclerViewData(stories: PagingData<Story>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        listAdapter.submitData(lifecycle, stories)
        Log.d(HomeFragment::class.java.simpleName, "Item count: ${listAdapter.itemCount}")
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

}
