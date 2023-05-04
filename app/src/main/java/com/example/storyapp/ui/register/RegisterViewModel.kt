package com.example.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _registerState: MutableStateFlow<ResultState<LoginResponse>> =
        MutableStateFlow(ResultState.Idle())
    val registerState = _registerState.asStateFlow()

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    fun register(name: String, email: String, password: String) {
        try {
            _registerState.value = ResultState.Loading()
            viewModelScope.launch {
                val result = ApiConfig().getApiService().register(hashMapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password
                ))

                if (!result.error) {
                    _token.value = result.loginResult?.token
                    _userId.value = result.loginResult?.userId
                }

                _registerState.value = ResultState.Success(result)
            }
        } catch (e: Exception) {
            _registerState.value = ResultState.Error(e.message ?: "")
        }
    }
}
