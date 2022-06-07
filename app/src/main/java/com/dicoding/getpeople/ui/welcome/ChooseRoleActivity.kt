package com.dicoding.getpeople.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.getpeople.databinding.ActivityChooseRoleBinding
import com.dicoding.getpeople.ui.signup.SignupPenggunaActivity
import com.dicoding.getpeople.ui.signup.SignupPetugasActivity

class ChooseRoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
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