package com.example.storyapp.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel: ViewModel() {
    fun saveAuthToken(token: String) {
        viewModelScope.launch {

        }
    }
}
