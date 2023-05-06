package com.example.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.storyapp.MainActivity
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.home.HomeFragment
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
            viewModel.loginState.collect { resultState ->
                Log.d(
                    LoginActivity::class.java.simpleName,
                    "State: ${resultState.javaClass}"
                )

                when (resultState) {
                    is ResultState.Error -> {
//                        TODO: Tadi kurang ini, jadinya loading-nya gak ke-hide pas error
                        binding.rvLoading.visibility = View.GONE

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

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
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
