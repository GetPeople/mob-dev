package com.dicoding.getpeople.ui.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.R
import com.dicoding.getpeople.databinding.ActivityMapsBinding
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.addVictim.AddVictimActivity
import com.dicoding.getpeople.ui.findVictim.FindVictimActivity
import com.dicoding.getpeople.ui.listVictim.ListVictimActivity
import com.dicoding.getpeople.ui.welcome.WelcomeActivity
import com.dicoding.getpeople.ui.welcome.dataStore

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var user : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val map = menu?.findItem(R.id.map_appbar)
        map?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout -> {
                mapsViewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun setupViewModel() {
        mapsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MapsViewModel::class.java]

//        mapsViewModel.getUser().observe(this) { user ->
//            this.user = user
//            if (!user.isLogin) {
//                val intent = Intent(this, WelcomeActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//            }
//        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val jakarta = LatLng(-6.2, 106.816)
        mMap.addMarker(MarkerOptions().position(jakarta).title("Marker in Jakarta"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 5f))
        mMap.setOnMarkerClickListener { _ ->
            if (user.role == "pengguna") {
                startActivity(Intent(this, FindVictimActivity::class.java))
            } else {
                startActivity(Intent(this, AddVictimActivity::class.java))
            }
            true
        }
    }
}