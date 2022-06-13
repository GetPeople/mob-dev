package com.dicoding.getpeople.ui.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.getpeople.data.repository.VictimRepository
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import okhttp3.MultipartBody

class LoadingViewModel(private val pref: UserPreference,
                       private val victimRepository: VictimRepository
) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun cariKorban(
        token : String,
        photo : MultipartBody.Part
    ) = victimRepository.cariKorban(token,photo)
}