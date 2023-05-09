//package com.example.storyapp.ui.home
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.paging.PagingData
//import com.example.storyapp.data.Story
//import com.example.storyapp.data.repository.StoryRepository
//import com.example.storyapp.data.response.ResultState
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//class HomeFragmentViewModel constructor(
//    private val homeGetStory: HomeGetStory,
//    storyRepository: StoryRepository
//): ViewModel() {
//    private val _storyResult = MutableStateFlow(HomeStoryResult())
//    val storyResult = _storyResult.asStateFlow()
//
//    fun getStories() {
//        viewModelScope.launch {
//            homeGetStory().collect { stories ->
//                _storyResult.update {
//                    it.copy(resultStories = stories)
//                }
//            }
//        }
//    }
//}
//
//data class HomeStoryResult(
//    val resultStories: ResultState<List<Story>> = ResultState.Idle(),
//    val username: String = "",
//)
//
