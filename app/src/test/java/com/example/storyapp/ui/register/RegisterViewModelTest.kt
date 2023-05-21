package com.example.storyapp.ui.register

import com.example.storyapp.data.repository.AuthRepository
import com.example.storyapp.data.response.LoginResponse
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


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var registerViewModel: RegisterViewModel

    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyName = "Full Name"
    private val dummyEmail = "email@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(authRepository)
    }

    @Test
    fun `Registration successfully`(): Unit = runTest {
        val expectedResponse = flowOf(Result.success(dummyRegisterResponse))

        Mockito.`when`(registerViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        registerViewModel.register(dummyName, dummyEmail, dummyPassword).collect { response ->

            assertTrue(response.isSuccess)
            assertFalse(response.isFailure)

            response.onSuccess { actualResponse ->
                assertNotNull(actualResponse)
                assertSame(dummyRegisterResponse, actualResponse)
            }
        }

        Mockito.verify(authRepository).register(dummyName, dummyEmail, dummyPassword)
    }

    @Test
    fun `Registration failed`(): Unit = runTest {
        val expectedResponse: Flow<Result<LoginResponse>> =
            flowOf(Result.failure(Exception("failed")))

        Mockito.`when`(registerViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        registerViewModel.register(dummyName, dummyEmail, dummyPassword).collect { response ->

            assertFalse(response.isSuccess)
            assertTrue(response.isFailure)

            response.onFailure {
                assertNotNull(it)
            }
        }
    }

}