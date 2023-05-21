package com.example.storyapp.ui.login

import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.data.repository.AuthRepository
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyToken = "authentication_token"
    private val dummyEmail = "kumaa@gmail.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `Login successfully`(): Unit = runTest {
        val expectedResponse = flow {
            emit(Result.success(dummyLoginResponse))
        }

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect { result ->

            assertTrue(result.isSuccess)
            assertFalse(result.isFailure)

            result.onSuccess { actualResponse ->
                assertNotNull(actualResponse)
                assertSame(dummyLoginResponse, actualResponse)
            }
        }

        Mockito.verify(authRepository).login(dummyEmail, dummyPassword)
    }

    @Test
    fun `Login failed`(): Unit = runTest {
        val expectedResponse: Flow<Result<LoginResponse>> =
            flowOf(Result.failure(Exception("login failed")))

        Mockito.`when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect { result ->

            assertFalse(result.isSuccess)
            assertTrue(result.isFailure)

            result.onFailure {
                assertNotNull(it)
            }
        }

        Mockito.verify(authRepository).login(dummyEmail, dummyPassword)
    }

    @Test
    fun `Save token successfully`(): Unit = runTest {
        loginViewModel.saveAuthToken(dummyToken)
        Mockito.verify(authRepository).saveAuthToken(dummyToken)
    }
}