package com.example.storyapp.ui.setting

import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
class SettingViewModelTest {

    @get:Rule
    var coroutinesTestRule = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var settingViewModel: SettingViewModel

    private val dummyToken = "authentication_token"

    @Before
    fun setup() {
        settingViewModel = SettingViewModel(authRepository)
    }

    @Test
    fun `Clear authentication token successfully`() = runBlockingTest {
        settingViewModel.saveAuthToken(dummyToken)
        Mockito.verify(authRepository).clearToken(dummyToken)
    }
}