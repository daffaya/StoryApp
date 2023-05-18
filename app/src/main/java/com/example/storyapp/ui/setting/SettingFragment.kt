package com.example.storyapp.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentSettingBinding
import com.example.storyapp.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
@Suppress("UNREACHABLE_CODE")
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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