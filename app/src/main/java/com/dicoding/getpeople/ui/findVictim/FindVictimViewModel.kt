package com.dicoding.getpeople.ui.findVictim

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.data.repository.VictimRepository
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class FindVictimViewModel(private val pref: UserPreference,
                          private val victimRepository: VictimRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun cariKorban(
        token : String,
        photo : MultipartBody.Part
    ) = victimRepository.cariKorban(token,photo)
}