package com.example.storyapp.ui.story

import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.data.repository.AuthRepository
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.FileUploadResponse
import com.example.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var createViewModel: AddStoryViewModel

    private val dummyToken = "authentication_token"
    private val dummyUploadResponse = DataDummy.generateDummyFileUploadResponse()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()

    @Before
    fun setup() {
        createViewModel = AddStoryViewModel(authRepository, storyRepository)
    }

    @Test
    fun `Getting token successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)

        Mockito.`when`(createViewModel.getAuthToken()).thenReturn(expectedToken)

        createViewModel.getAuthToken().collect { actualToken ->
            assertNotNull(actualToken)
            assertEquals(dummyToken, actualToken)
        }

        Mockito.verify(authRepository).getAuthToken()
        Mockito.verifyNoInteractions(storyRepository)
    }

    @Test
    fun `Getting token successfully but null`() = runTest {
        val expectedToken = flowOf(null)

        Mockito.`when`(createViewModel.getAuthToken()).thenReturn(expectedToken)

        createViewModel.getAuthToken().collect { actualToken ->
            assertNull(actualToken)
        }

        Mockito.verify(authRepository).getAuthToken()
        Mockito.verifyNoInteractions(storyRepository)
    }

    @Test
    fun `Upload file successfully`() = runTest {
        val expectedResponse = flowOf(Result.success(dummyUploadResponse))

        Mockito.`when`(
            createViewModel.uploadImage(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(expectedResponse)

        createViewModel.uploadImage(dummyToken, dummyMultipart, dummyDescription, null, null)
            .collect { result ->

                assertTrue(result.isSuccess)
                assertFalse(result.isFailure)

                result.onSuccess { actualResponse ->
                    assertNotNull(actualResponse)
                    assertSame(dummyUploadResponse, actualResponse)
                }
            }

        Mockito.verify(storyRepository)
            .uploadStory(dummyToken, dummyMultipart, dummyDescription, null, null)
        Mockito.verifyNoInteractions(authRepository)
    }

    @Test
    fun `Upload file failed`(): Unit = runTest {
        val expectedResponse: Flow<Result<FileUploadResponse>> =
            flowOf(Result.failure(Exception("failed")))

        Mockito.`when`(
            createViewModel.uploadImage(
                dummyToken,
                dummyMultipart,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(expectedResponse)

        createViewModel.uploadImage(dummyToken, dummyMultipart, dummyDescription, null, null)
            .collect { result ->
                assertFalse(result.isSuccess)
                assertTrue(result.isFailure)

                result.onFailure { actualResponse ->
                    assertNotNull(actualResponse)
                }
            }

    }
}