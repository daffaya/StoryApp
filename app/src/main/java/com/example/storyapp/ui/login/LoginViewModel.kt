package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.utils.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginState: MutableStateFlow<ResultState<LoginResponse>> =
        MutableStateFlow(ResultState.Idle())
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        try {
            _loginState.value = ResultState.Loading()
            viewModelScope.launch {
                val result = ApiConfig().getApiService().login(
                    hashMapOf(
                        "email" to email,
                        "password" to password
                    )
                )
                if (result.code() == 200) {
                    result.body()?.let {
//                        TODO: Dude, you should save the token and userId in Preference/DataStore
//                        Refrences:
//                        https://developer.android.com/topic/libraries/architecture/datastore
//                        https://medium.com/supercharges-mobile-product-guide/new-way-of-storing-data-in-android-jetpack-datastore-a1073d09393d
                        _loginState.value = ResultState.Success(it)
                    }
                } else {
//                    TODO: Buat parsing errorBody()
                    val errorBody = Gson().fromJson(
                        result.errorBody()?.charStream(),
                        LoginResponse::class.java
                    )
                    _loginState.value = ResultState.Error(errorBody.message)
                }
            }
        } catch (e: Exception) {
//            TODO: Lebih buat nangkep error semisal Internet gak nyambung, server dari sono unaccessible, dll.
            _loginState.value = ResultState.Error(e.message ?: "")
        }
    }
}
