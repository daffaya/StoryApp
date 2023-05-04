package com.example.storyapp.ui.register

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
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.utils.ResultState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { resultState ->
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
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            if (!binding.edRegisterEmail.isEmailValid()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else if (binding.edRegisterName.text!!.isBlank()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val name = binding.edRegisterName.text.toString().trim()
                val email = binding.edRegisterEmail.text.toString().trim()
                val password = binding.edRegisterPassword.text.toString().trim()
                viewModel.register(name, email, password)
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(
                Intent(
                    this, LoginActivity::class.java
                )
            )
        }
    }
}
