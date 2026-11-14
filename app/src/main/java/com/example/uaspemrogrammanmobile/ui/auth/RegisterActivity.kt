package com.example.uaspemrogrammanmobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.uaspemrogrammanmobile.MainActivity
import com.example.uaspemrogrammanmobile.R
import com.example.uaspemrogrammanmobile.databinding.ActivityRegisterBinding
import com.example.uaspemrogrammanmobile.utils.Resource

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(name, email, password)) {
                viewModel.register(name, email, password)
            }
        }

        binding.tvGoLogin.setOnClickListener {
            finish() // Kembali ke LoginActivity
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        binding.tilName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        if (name.isEmpty()) {
            binding.tilName.error = getString(R.string.err_empty_name)
            return false
        }
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.err_empty_email)
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.err_invalid_email)
            return false
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.err_empty_password)
            return false
        }
        if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.err_short_password)
            return false
        }
        return true
    }

    private fun observeViewModel() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}