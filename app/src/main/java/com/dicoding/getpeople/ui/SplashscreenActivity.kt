package com.dicoding.getpeople.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.getpeople.databinding.ActivitySplashscreenBinding
import com.dicoding.getpeople.ui.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val mainIntent = Intent(this, WelcomeActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}