package com.dicoding.getpeople.ui.searchResult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.R
import com.dicoding.getpeople.databinding.ActivitySearchResultBinding
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.listVictim.ListVictimViewModel
import com.dicoding.getpeople.ui.maps.MapsActivity
import com.dicoding.getpeople.ui.welcome.WelcomeActivity
import com.dicoding.getpeople.ui.welcome.dataStore

class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var searchResultViewModel: SearchResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.map_appbar -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            R.id.logout -> {
                searchResultViewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun setupViewModel(){
        searchResultViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SearchResultViewModel::class.java]

        searchResultViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}