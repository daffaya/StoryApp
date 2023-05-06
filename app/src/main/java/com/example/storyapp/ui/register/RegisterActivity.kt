package com.example.storyapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var registerJob: Job = Job()
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        redirect()
    }
//        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.registerState.collect { resultState ->
//                    when (resultState) {
//                        is ResultState.Error -> {
//                            Snackbar.make(
//                                binding.root,
//                                resultState.message,
//                                Snackbar.LENGTH_LONG
//                            ).show()
//                        }
//                        is ResultState.Idle -> {
//                            // do nothing, just stay idle
//                        }
//                        is ResultState.Loading -> {
//                            binding.rvLoading.visibility = View.VISIBLE
//                        }
//                        is ResultState.Success -> {
//                            binding.rvLoading.visibility = View.GONE
//                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                    }
//                }
//            }
//        }

//

    private fun redirect() {
        binding.apply {
            tvLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            btnRegister.setOnClickListener {
                handleRegister()
            }
        }
    }

    private fun handleRegister() {
        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString()
        showLoading(true)

        lifecycleScope.launchWhenResumed {
            if (registerJob.isActive) registerJob.cancel()

            registerJob = launch {
                viewModel.register(name, email, password).collect { result ->
                    result.onSuccess {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.register_success_msg),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    result.onFailure {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.registrater_error_msg),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            edRegisterName.isEnabled = !isLoading
            edRegisterEmail.isEnabled = !isLoading
            edRegisterPassword.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading

            if (isLoading) {
                rvLoading.visibility = View.VISIBLE
            } else {
                rvLoading.visibility = View.GONE
            }
        }
    }
}
