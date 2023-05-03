package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel(){
    private val _loginState = MutableStateFlow(LoginViewState())
    val loginState = _loginState.asStateFlow()


}