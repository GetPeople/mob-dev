package com.dicoding.getpeople.ui.listVictim

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.getpeople.R
import com.dicoding.getpeople.data.Result
import com.dicoding.getpeople.data.remote.response.KorbanItem
import com.dicoding.getpeople.databinding.ActivityListVictimBinding
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.adapter.ListVictimAdapter
import com.dicoding.getpeople.ui.addVictim.AddVictimActivity
import com.dicoding.getpeople.ui.detailVictim.DetailVictimActivity
import com.dicoding.getpeople.ui.findVictim.FindVictimViewModel
import com.dicoding.getpeople.ui.maps.MapsActivity
import com.dicoding.getpeople.ui.welcome.WelcomeActivity
import com.dicoding.getpeople.ui.welcome.dataStore

class ListVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListVictimBinding
    private lateinit var listVictimViewModel: ListVictimViewModel
    private lateinit var user : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListVictimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupAction()
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
                listVictimViewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun setupViewModel(){
        listVictimViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[ListVictimViewModel::class.java]

        listVictimViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                this.user = user
            }
        }
    }

    private fun setupRecyclerView() {
        listVictimViewModel.getListKorban(user.token).observe(this) { response ->
            if (response != null) {
                when(response) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setupListKorban(response.data.listKorban as List<KorbanItem>)
                    }
                    is Result.Error -> {
                        AlertDialog.Builder(this).apply {
                            setTitle(getString(R.string.gagal))
                            setMessage(response.error)
                            setNegativeButton(getString(R.string.tutup)) { _, _ ->
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun setupListKorban(listKorban : List<KorbanItem>) {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvResult.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvResult.layoutManager = LinearLayoutManager(this)
        }
        val adapter = ListVictimAdapter(listKorban)
        binding.rvResult.adapter = adapter
        adapter.setOnItemClickCallback(object : ListVictimAdapter.OnItemClickCallback {
            override fun onItemClicked(korban: KorbanItem) {
                val intent = Intent(this@ListVictimActivity, DetailVictimActivity::class.java)
                intent.putExtra(DetailVictimActivity.KORBAN, korban)
                startActivity(intent)
            }

        })
    }

    private fun setupAction() {
        binding.buttonTambahKorban.setOnClickListener {
            val intent = Intent(this, AddVictimActivity::class.java)
            startActivity(intent)
        }
    }
}