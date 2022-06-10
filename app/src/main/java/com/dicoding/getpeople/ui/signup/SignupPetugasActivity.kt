package com.dicoding.getpeople.ui.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.R
import com.dicoding.getpeople.data.Result
import com.dicoding.getpeople.databinding.ActivitySignupPetugasBinding
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.isEmailValid
import com.dicoding.getpeople.ui.login.LoginActivity
import com.dicoding.getpeople.ui.welcome.dataStore

class SignupPetugasActivity : AppCompatActivity() {

    private lateinit var signupViewModel: SignupViewModel
    private lateinit var binding: ActivitySignupPetugasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPetugasBinding.inflate(layoutInflater)
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
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]
    }

    private fun setupAction() {

        binding.editTextName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.textInputLayoutName.error = null
            } else {
                if (binding.editTextName.text.toString() == "") {
                    binding.textInputLayoutName.error = getString(R.string.jangan_kosong)
                } else {
                    binding.textInputLayoutName.error = null
                }
            }
        }

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

        binding.editTextId.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.textInputLayoutId.error = null
            } else {
                if (binding.editTextId.text.toString() == "") {
                    binding.textInputLayoutId.error = getString(R.string.jangan_kosong)
                } else {
                    binding.textInputLayoutId.error = null
                }
            }
        }

        binding.buttonSignup.setOnClickListener {
            val idPetugas = binding.editTextId.text.toString()
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            when {
                idPetugas.isEmpty() -> {}
                name.isEmpty() -> {}
                email.isEmpty() -> {}
                password.isEmpty() -> {}
                password.length < 6 -> {
                    binding.textInputLayoutPassword.error = getString(R.string.min_enam)
                }
                !isEmailValid(email) -> {
                    binding.textInputLayoutEmail.error = getString(R.string.email_tidak_valid)
                }
                else -> {
                    signupViewModel.register(name, email, password, "petugas", idPetugas).observe(this) { result ->
                        if (result != null) {
                            when(result) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this).apply {
                                        setTitle(getString(R.string.berhasil))
                                        setMessage(result.data.message)
                                        setPositiveButton(getString(R.string.lanjut)) { _, _ ->
                                            val intent = Intent(this@SignupPetugasActivity, LoginActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        }
                                        create()
                                        show()
                                    }
                                }
                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this).apply {
                                        setTitle(getString(R.string.gagal))
                                        setMessage(result.error)
                                        setNegativeButton(getString(R.string.tutup)) { _, _ ->
                                        }
                                        create()
                                        show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}