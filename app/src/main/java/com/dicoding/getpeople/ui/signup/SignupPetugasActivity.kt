package com.dicoding.getpeople.ui.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.databinding.ActivitySignupPetugasBinding
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.login.LoginActivity
import com.dicoding.getpeople.ui.welcome.dataStore

class SignupPetugasActivity : AppCompatActivity() {

    private lateinit var signupViewModel: SignupViewModel
    private lateinit var binding: ActivitySignupPetugasBinding
    private val trueIdPetugas = "12345"

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
        binding.buttonSignup.setOnClickListener {
            val idPetugas = binding.editTextId.text.toString()
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            when {
                idPetugas.isEmpty() -> {
                    binding.textInputLayoutId.error = "Masukkan ID petugas"
                }
                name.isEmpty() -> {
                    binding.textInputLayoutName.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.textInputLayoutEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.textInputLayoutPassword.error = "Masukkan password"
                }
                idPetugas != trueIdPetugas -> {
                    binding.textInputLayoutId.error = "ID tidak valid"
                }
                else -> {
                    signupViewModel.saveUser(UserModel(name, email,false, password, "petugas"))
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akun berhasil dibuat!")
                        setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(this@SignupPetugasActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

}