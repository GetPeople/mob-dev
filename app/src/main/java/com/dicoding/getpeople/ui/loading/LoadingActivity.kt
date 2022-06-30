package com.dicoding.getpeople.ui.loading

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.R
import com.dicoding.getpeople.data.Result
import com.dicoding.getpeople.data.remote.response.KorbanItem
import com.dicoding.getpeople.databinding.ActivityLoadingBinding
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.ViewModelFactory
import com.dicoding.getpeople.ui.reduceFileImage
import com.dicoding.getpeople.ui.searchResult.SearchResultActivity
import com.dicoding.getpeople.ui.welcome.dataStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    private lateinit var loadingViewModel: LoadingViewModel
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupView()
        setupViewModel()
        playAnimation()
        cariKorban()
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
        loadingViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(UserPreference.getInstance(dataStore))
        )[LoadingViewModel::class.java]

        loadingViewModel.getUser().observe(this) { user ->
            token = user.token
        }
    }

    private fun playAnimation() {
        val image = ObjectAnimator.ofFloat(binding.imageLoading, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val text = ObjectAnimator.ofFloat(binding.textMencari, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        AnimatorSet().apply {
            playTogether(image, text)
            start()
        }
    }

    private fun cariKorban() {
        val getFile = intent.extras?.get(PICTURE)
        val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "find",
            file.name,
            requestImageFile
        )
        loadingViewModel.cariKorban(token, imageMultipart).observe(this) { response ->
            if (response != null) {
                when(response) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        val intent = Intent(this@LoadingActivity, SearchResultActivity::class.java)
                        val arrayListKorban = arrayListOf<KorbanItem>()
                        val listKorban = response.data.listKorban
                        arrayListKorban.addAll(listKorban as List<KorbanItem>)
                        intent.putParcelableArrayListExtra(SearchResultActivity.LIST_KORBAN, arrayListKorban)
                        startActivity(intent)
                        finish()
                    }
                    is Result.Error -> {
                        AlertDialog.Builder(this).apply {
                            setTitle(getString(R.string.gagal))
                            setMessage(response.error)
                            setNegativeButton(getString(R.string.tutup)) { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }

    }

    companion object{
        const val PICTURE = "picture"
    }
}