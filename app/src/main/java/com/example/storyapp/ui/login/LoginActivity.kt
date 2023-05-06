package com.example.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.MainActivity
import com.example.storyapp.R
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var loginJob: Job = Job()

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        redirect()

    }

    private fun redirect() {
        binding.apply {
            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            btnLogin.setOnClickListener {
                handleLogin()
            }
        }
    }

    private fun handleLogin() {
        val email = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString()
        showLoading(true)

        lifecycleScope.launchWhenResumed {
            if (loginJob.isActive) loginJob.cancel()

            loginJob = launch {
                viewModel.login(email, password).collect { result ->
                    result.onSuccess { authentication ->
                        val loginResult = authentication.loginResult
                        if (loginResult != null) {
                            val token = loginResult.token
                            if (token != null) {
                                viewModel.saveAuthToken(token)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra(EXTRA_TOKEN, token)
                                startActivity(intent)
                                finish()
                            }

                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.login_success_msg),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        result.onFailure {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.login_error_msg),
                                Snackbar.LENGTH_SHORT
                            ).show()

                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    fun showLoading(isLoading: Boolean) {
        binding.apply {
            edLoginEmail.isEnabled = !isLoading
            edLoginPassword.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            // Animate views alpha
            if (isLoading) {
                rvLoading.visibility = View.VISIBLE
            } else {
                rvLoading.visibility = View.GONE
            }
        }
    }
}
