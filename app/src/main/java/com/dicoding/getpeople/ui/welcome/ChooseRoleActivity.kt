package com.dicoding.getpeople.ui.welcome

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.getpeople.databinding.ActivityChooseRoleBinding
import com.dicoding.getpeople.ui.signup.SignupPenggunaActivity
import com.dicoding.getpeople.ui.signup.SignupPetugasActivity

class ChooseRoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
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

    private fun setupAction() {
        binding.buttonPengguna.setOnClickListener {
            startActivity(Intent(this, SignupPenggunaActivity::class.java))
        }
        binding.buttonPetugas.setOnClickListener {
            startActivity(Intent(this, SignupPetugasActivity::class.java))
        }
    }
}