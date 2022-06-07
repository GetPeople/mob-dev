package com.dicoding.getpeople.ui.detailVictim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.getpeople.databinding.ActivityDetailVictimBinding

class DetailVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailVictimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVictimBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}