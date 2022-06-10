package com.dicoding.getpeople.ui.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.R
import com.dicoding.getpeople.databinding.ActivityLoginBinding
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.isEmailValid
import com.dicoding.getpeople.ui.maps.MapsActivity
import com.dicoding.getpeople.ui.welcome.dataStore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun setupAction() {

        binding.editTextEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.textInputLayoutEmail.error = null
            } else {
                if (binding.editTextEmail.text.toString() == "") {
                    binding.textInputLayoutEmail.error = getString(R.string.jangan_kosong)
                } else {
                    binding.textInputLayoutEmail.error = null
                }
            }
        }

        binding.editTextPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.textInputLayoutPassword.error = null
            } else {
                if (binding.editTextPassword.text.toString() == "") {
                    binding.textInputLayoutPassword.error = getString(R.string.jangan_kosong)
                } else {
                    binding.textInputLayoutPassword.error = null
                }
            }
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            when {
                email.isEmpty() -> {}
                password.isEmpty() -> {}
                password.length < 6 -> {
                    binding.textInputLayoutPassword.error = getString(R.string.min_enam)
                }
                !isEmailValid(email) -> {
                    binding.textInputLayoutEmail.error = getString(R.string.email_tidak_valid)
                }
                else -> {
                    loginViewModel.login()
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                        setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(context, MapsActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

}