package com.dicoding.getpeople.ui.listVictim

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.data.repository.UserRepository
import com.dicoding.getpeople.data.repository.VictimRepository
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ListVictimViewModel(private val pref: UserPreference,
                          private val victimRepository: VictimRepository)
    : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getListKorban(token : String) = victimRepository.getListKorban(token)
}