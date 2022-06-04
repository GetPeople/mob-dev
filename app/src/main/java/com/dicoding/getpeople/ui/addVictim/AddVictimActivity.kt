package com.dicoding.getpeople.ui.addVictim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.getpeople.databinding.ActivityAddVictimBinding

class AddVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVictimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVictimBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}