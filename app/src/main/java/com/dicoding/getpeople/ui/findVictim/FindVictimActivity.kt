package com.dicoding.getpeople.ui.findVictim

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.R
import com.dicoding.getpeople.databinding.ActivityFindVictimBinding
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.addVictim.AddVictimActivity
import com.dicoding.getpeople.ui.detailVictim.DetailVictimViewModel
import com.dicoding.getpeople.ui.maps.MapsActivity
import com.dicoding.getpeople.ui.uriToFile
import com.dicoding.getpeople.ui.welcome.dataStore
import java.io.File

class FindVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindVictimBinding
    private lateinit var findVictimViewModel: FindVictimViewModel
    private var getFile: File? = null
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile
            binding.imageviewKorban.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindVictimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.menu_cari_korban)
//        if (!allPermissionsGranted()) {
//            ActivityCompat.requestPermissions(
//                this,
//                REQUIRED_PERMISSIONS,
//                REQUEST_CODE_PERMISSIONS
//            )
//        }

        setupViewModel()
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
                findVictimViewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun setupViewModel(){
        findVictimViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[FindVictimViewModel::class.java]
    }

    private fun setupAction() {
        binding.buttonUnggah.setOnClickListener { startGallery() }
    }

//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.pilih_gambar))
        launcherIntentGallery.launch(chooser)
    }

//    companion object {
//        const val CAMERA_X_RESULT = 200
//        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//        private const val REQUEST_CODE_PERMISSIONS = 10
//    }
}