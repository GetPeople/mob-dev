package com.dicoding.getpeople.ui.findVictim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.getpeople.databinding.ActivityFindVictimBinding

class FindVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindVictimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindVictimBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}