package com.dicoding.getpeople.ui.searchResult

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.getpeople.R
import com.dicoding.getpeople.data.remote.response.KorbanItem
import com.dicoding.getpeople.databinding.ActivitySearchResultBinding
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.adapter.ListVictimAdapter
import com.dicoding.getpeople.ui.detailVictim.DetailVictimActivity
import com.dicoding.getpeople.ui.listVictim.ListVictimViewModel
import com.dicoding.getpeople.ui.maps.MapsActivity
import com.dicoding.getpeople.ui.welcome.WelcomeActivity
import com.dicoding.getpeople.ui.welcome.dataStore
import com.google.android.material.divider.MaterialDividerItemDecoration

class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var searchResultViewModel: SearchResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
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
            ViewModelFactory.getInstance(UserPreference.getInstance(dataStore))
        )[SearchResultViewModel::class.java]

        searchResultViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun setupRecyclerView() {
        val arrayListKorban = intent.getParcelableArrayListExtra<KorbanItem>(LIST_KORBAN)
        if (arrayListKorban != null) {
            setupListKorban(arrayListKorban.toList())
        }
    }

    private fun setupListKorban(listKorban : List<KorbanItem>) {
        var layoutManager = LinearLayoutManager(this)
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = GridLayoutManager(this, 2)
        }

        binding.rvResult.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvResult.addItemDecoration(itemDecoration)

        val adapter = ListVictimAdapter(listKorban)
        binding.rvResult.adapter = adapter

        adapter.setOnItemClickCallback(object : ListVictimAdapter.OnItemClickCallback {
            override fun onItemClicked(korban: KorbanItem) {
                val intent = Intent(this@SearchResultActivity, DetailVictimActivity::class.java)
                intent.putExtra(DetailVictimActivity.KORBAN, korban)
                startActivity(intent)
            }
        })
    }

    companion object{
        const val LIST_KORBAN = "list korban"
    }
}