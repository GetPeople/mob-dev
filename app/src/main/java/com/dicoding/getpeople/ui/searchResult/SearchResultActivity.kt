package com.dicoding.getpeople.ui.searchResult

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.getpeople.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}