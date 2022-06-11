package com.dicoding.getpeople.ui.findVictim

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.R
import com.dicoding.getpeople.data.Result
import com.dicoding.getpeople.data.remote.response.KorbanItem
import com.dicoding.getpeople.databinding.ActivityFindVictimBinding
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.addVictim.AddVictimActivity
import com.dicoding.getpeople.ui.camera.CameraActivity
import com.dicoding.getpeople.ui.detailVictim.DetailVictimViewModel
import com.dicoding.getpeople.ui.maps.MapsActivity
import com.dicoding.getpeople.ui.reduceFileImage
import com.dicoding.getpeople.ui.rotateBitmap
import com.dicoding.getpeople.ui.searchResult.SearchResultActivity
import com.dicoding.getpeople.ui.uriToFile
import com.dicoding.getpeople.ui.welcome.WelcomeActivity
import com.dicoding.getpeople.ui.welcome.dataStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class FindVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindVictimBinding
    private lateinit var findVictimViewModel: FindVictimViewModel
    private lateinit var user : UserModel
    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AddVictimActivity.CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.imageviewKorban.setImageBitmap(result)
        }
    }
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
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

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

        findVictimViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun setupAction() {
        binding.buttonAmbil.setOnClickListener { startCamera() }
        binding.buttonUnggah.setOnClickListener { startGallery() }
        binding.buttonCari.setOnClickListener { cariKorban() }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.pilih_gambar))
        launcherIntentGallery.launch(chooser)
    }

    private fun cariKorban() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            findVictimViewModel.cariKorban(user.token, imageMultipart).observe(this) { response ->
                if (response != null) {
                    when(response) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val intent = Intent(this@FindVictimActivity, SearchResultActivity::class.java)
                            val arrayListKorban = arrayListOf<KorbanItem>()
                            val listKorban = response.data.listKorban
                            arrayListKorban.addAll(listKorban as List<KorbanItem>)
                            intent.putParcelableArrayListExtra(SearchResultActivity.LIST_KORBAN, arrayListKorban)
                            startActivity(intent)
                            finish()
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
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
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.gagal))
                setMessage(getString(R.string.belum_ada_gambar))
                setNegativeButton(getString(R.string.tutup)) { _, _ ->
                }
                create()
                show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.izin_tidak_diberikan),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}