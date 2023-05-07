package com.example.storyapp.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.repository.AuthPreferencesDataStore
import com.example.storyapp.data.repository.AuthRepository
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.databinding.FragmentSettingBinding
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.login.LoginViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@Suppress("UNREACHABLE_CODE")
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var authPreferencesDataStore: AuthPreferencesDataStore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("auth")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        authPreferencesDataStore = AuthPreferencesDataStore(requireContext().dataStore)

        val repository = AuthRepository(
            ApiConfig().getApiService(),
            authPreferencesDataStore
        )
        val viewModelFactory = SettingViewModelFactory(repository)

        settingViewModel = ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogout.setOnClickListener {
                logoutAlert()
            }
        }

    }

    private fun logoutAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.logout_alert))
            .setMessage(getString(R.string.logout_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.logout)) { _, _ ->
                settingViewModel.saveAuthToken("")
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
            .show()
    }
}