package com.dicoding.getpeople.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.R
import com.dicoding.getpeople.databinding.ActivityWelcomeBinding
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: WelcomeViewModel
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
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
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[WelcomeViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                this.user = user
//                binding.welcomeUser.text = getString(R.string.welcome_user, user.name)
//                setupRecyclerView()
            }
        }

    }

    private fun setupAction() {
        binding.buttonLogin.setOnClickListener {
            //startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.buttonSignup.setOnClickListener {
            //startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}