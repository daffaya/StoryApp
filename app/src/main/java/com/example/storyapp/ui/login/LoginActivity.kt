package com.example.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.storyapp.MainActivity
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.story.StoryActivity
import com.example.storyapp.utils.ResultState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { resultState ->
                    when (resultState) {
                        is ResultState.Error -> {
                            Snackbar.make(
                                binding.root,
                                resultState.message,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is ResultState.Idle -> {
                            // do nothing, just stay idle
                        }
                        is ResultState.Loading -> {
                            binding.rvLoading.visibility = View.VISIBLE
                        }
                        is ResultState.Success -> {
                            binding.rvLoading.visibility = View.GONE
                            val intent = Intent(this@LoginActivity, StoryActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            if (!binding.edLoginEmail.isEmailValid()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else {
                val email = binding.edLoginEmail.text.toString().trim()
                val password = binding.edLoginPassword.text.toString().trim()
                viewModel.login(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(
                Intent(
                    this, RegisterActivity::class.java
                )
            )
        }
    }
}

