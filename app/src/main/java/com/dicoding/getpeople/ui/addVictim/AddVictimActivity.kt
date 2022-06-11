package com.dicoding.getpeople.ui.addVictim

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
import com.dicoding.getpeople.databinding.ActivityAddVictimBinding
import com.dicoding.getpeople.R
import com.dicoding.getpeople.data.Result
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.camera.CameraActivity
import com.dicoding.getpeople.ui.listVictim.ListVictimActivity
import com.dicoding.getpeople.ui.maps.MapsActivity
import com.dicoding.getpeople.ui.reduceFileImage
import com.dicoding.getpeople.ui.rotateBitmap
import com.dicoding.getpeople.ui.uriToFile
import com.dicoding.getpeople.ui.welcome.WelcomeActivity
import com.dicoding.getpeople.ui.welcome.dataStore
import com.google.android.material.datepicker.MaterialDatePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddVictimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVictimBinding
    private lateinit var addVictimViewModel: AddVictimViewModel
    private lateinit var user : UserModel
    private var getFile:File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
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
            val myFile = uriToFile(selectedImg, this@AddVictimActivity)

            getFile = myFile
            binding.imageviewKorban.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVictimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.menu_tambah_korban)
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
                addVictimViewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun setupViewModel(){
        addVictimViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AddVictimViewModel::class.java]

        addVictimViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                this.user = user
            }
        }
    }

    private fun setupAction() {
        binding.editTextTanggalLahir.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih tanggal lahir")
                    .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                    .build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                val date = dateFormatter.format(Date(it))
                binding.editTextTanggalLahir.setText(date)
            }
        }

        binding.editTextLokasi.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.textInputLayoutLokasi.error = null
            } else {
                if (binding.editTextLokasi.text.toString() == "") {
                    binding.textInputLayoutLokasi.error = getString(R.string.jangan_kosong)
                } else {
                    binding.textInputLayoutLokasi.error = null
                }
            }
        }

        binding.editTextKontak.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.textInputLayoutKontak.error = null
            } else {
                if (binding.editTextKontak.text.toString() == "") {
                    binding.textInputLayoutKontak.error = getString(R.string.jangan_kosong)
                } else {
                    binding.textInputLayoutKontak.error = null
                }
            }
        }

        binding.buttonAmbil.setOnClickListener { startCamera() }
        binding.buttonUnggah.setOnClickListener { startGallery() }
        binding.buttonKirim.setOnClickListener { tambahKorban() }
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

    private fun tambahKorban() {
        if (getFile != null) {
            val posko = binding.editTextLokasi.text.toString()
            val kontak = binding.editTextKontak.text.toString()
            val name = binding.editTextNama.text.toString()
            val gender = binding.editTextGender.text.toString()
            val birthPlace = binding.editTextTempatLahir.text.toString()
            val birthDate = binding.editTextTanggalLahir.text.toString()
            val momName = binding.editTextIbu.text.toString()
            val nik = binding.editTextNik.text.toString()

            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            when {
                posko.isEmpty() -> {}
                kontak.isEmpty() -> {}
                else -> {
                    addVictimViewModel.tambahKorban(
                        user.token,
                        imageMultipart,
                        posko,
                        kontak,
                        name,
                        gender,
                        birthPlace,
                        birthDate,
                        momName,
                        nik
                    ).observe(this) { response ->
                        if (response != null) {
                            when(response) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this).apply {
                                        setTitle(getString(R.string.berhasil))
                                        setMessage(response.data.message)
                                        setPositiveButton(getString(R.string.lanjut)) { _, _ ->
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}