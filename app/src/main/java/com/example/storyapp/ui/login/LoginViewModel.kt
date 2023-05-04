package com.example.storyapp.ui.login

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

class LoginViewModel : ViewModel() {

    private val _loginState: MutableStateFlow<ResultState<LoginResponse>> =
        MutableStateFlow(ResultState.Idle())
    val loginState = _loginState.asStateFlow()

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    fun login(email: String, password: String) {
        try {
            _loginState.value = ResultState.Loading()
            viewModelScope.launch {
                val result = ApiConfig().getApiService().login(hashMapOf(
                    "email" to email,
                    "password" to password
                ))

                if (!result.error) {
                    _token.value = result.loginResult?.token
                    _userId.value = result.loginResult?.userId
                }

                _loginState.value = ResultState.Success(result)
            }
        } catch (e: Exception) {
            _loginState.value = ResultState.Error(e.message ?: "")
        }
    }
}
