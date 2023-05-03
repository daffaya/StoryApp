package com.example.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.utils.ResultState
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    //    TODO: Initiate viewModel
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

//        TODO: Remember to call this lifecycleScope.launch whenever u collect a flow like loginState from viewModel
//        Oiya, collect flow itu sama kayak observe LiveData
        lifecycleScope.launch {
//           TODO: Simply, jadinya collecting state cuma repeat pas Lifecycle Started
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { resultState ->
//                    TODO: Here you can do whatever you want according to state
//                    Kalo error tampilin snackbar, kalo loading progress bar, kalo success redirect page
                    when (resultState) {
                        is ResultState.Error -> TODO()
                        is ResultState.Idle -> TODO()
                        is ResultState.Loading -> TODO()
                        is ResultState.Success -> TODO()
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            if (!binding.edLoginEmail.isEmailValid()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else {
                // Perform login
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
