package com.dicoding.getpeople.ui.listVictim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.getpeople.databinding.ActivityListVictimBinding

class ListVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListVictimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListVictimBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}