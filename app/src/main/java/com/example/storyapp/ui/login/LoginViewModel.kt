package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    //    TODO: Kayaknya state-nya mending manfaatin ResultState.kt
    //    Gunanya state buat apa? Biar enak nanti buat nampilin state dari login ke user
    //    Kayak nampilin loading pas lagi ResultState.Loading, atau munculin popup pas login-nya error di ResultState.Error
    private val _loginState: MutableStateFlow<ResultState<LoginResponse>> =
        MutableStateFlow(ResultState.Idle())
    val loginState = _loginState.asStateFlow()

    //    TODO: Bikin fungsi login()
    fun login(email: String, password: String) {
//        TODO: Manfaatin try-catch block buat nangkep error
        try {
//            Nandain state-nya lagi loading
//            Tampilin loading progress bar ke user
            _loginState.value = ResultState.Loading()
//            TODO: try isinya call ke API-nya
            viewModelScope.launch {
//                TODO: Simpen responsenya
                val result = ApiConfig().getApiService().login(hashMapOf())
//                TODO: Abis itu simpen token sama userId di prefrences (atau datastore)
//                Do simpen token sama userId here...

//                Nandain kalo login succes
//                Nanti kalo state-nya sukses, berarti redirect ke halaman main
                _loginState.value = ResultState.Success(result)
            }
        } catch (e: Exception) {
//            TODO: In case ada error (salah password atau email) larinya kesini
//            Ini buat nandain kalo state-nya error
//            Tampilin pesan error di Activity-nya, prefer pake Dialog atau snackbar
            _loginState.value = ResultState.Error(e.message ?: "")
        }
    }
}