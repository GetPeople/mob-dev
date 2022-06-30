package com.dicoding.getpeople.ui.detailVictim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.getpeople.R
import com.dicoding.getpeople.data.remote.response.KorbanItem
import com.dicoding.getpeople.databinding.ActivityDetailVictimBinding
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.maps.MapsActivity
import com.dicoding.getpeople.ui.welcome.WelcomeActivity
import com.dicoding.getpeople.ui.welcome.dataStore

class DetailVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailVictimBinding
    private lateinit var detailVictimViewModel: DetailVictimViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVictimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.menu_detail_korban)

        setupViewModel()
        setupView()
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
                detailVictimViewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun setupViewModel(){
        detailVictimViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(UserPreference.getInstance(dataStore))
        )[DetailVictimViewModel::class.java]

        detailVictimViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun setupView() {
        val victim = intent.getParcelableExtra<KorbanItem>(KORBAN)
        Glide.with(this)
            .load(victim?.photoUrl)
            .into(binding.imageviewKorban)
        binding.apply {
            valueLokasi.text = victim?.posko
            valueKontak.text = victim?.contact
            valueNama.text = victim?.name
            valueGender.text = victim?.gender
            valueTempatLahir.text = victim?.birthPlace
            valueTanggalLahir.text = victim?.birthDate
            valueIbu.text = victim?.momName
            valueNik.text = victim?.nik
        }
    }

    companion object {
        const val KORBAN = "korban"
    }
}