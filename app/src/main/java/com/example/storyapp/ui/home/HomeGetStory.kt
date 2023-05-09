//package com.example.storyapp.ui.home
//
//import com.example.storyapp.data.Story
//import com.example.storyapp.data.repository.StoryRepository
//import com.example.storyapp.data.response.ResultState
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.map
//
//class HomeGetStory(private val storyRepository: StoryRepository) {
//    operator fun invoke(): Flow<ResultState<List<Story>>> = flow {
//        emit(ResultState.Loading())
//        storyRepository.getStories().map { response ->
//            response.storyResponseItems.map { story ->
//                Story(
//                    id = story.id,
//                    name = story.name,
//                    description = story.description,
//                    photoUrl = story.photoUrl,
//                    createdAt = story.createdAt,
//                )
//            }
//        }.catch { exception ->
//            emit(ResultState.Error(message = exception.message.toString()))
//        }.collect { stories ->
//            emit(ResultState.Success(stories))
//        }
//    }
//}
