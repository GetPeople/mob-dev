package com.dicoding.getpeople.ui.addVictim

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.data.repository.VictimRepository
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AddVictimViewModel(private val pref: UserPreference,
                         private val victimRepository: VictimRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun tambahKorban(
        token: String,
        photo : MultipartBody.Part,
        posko : String,
        kontak : String,
        name : String,
        gender : String,
        birthPlace : String,
        birthDate : String,
        momName : String,
        nik : String
    ) = victimRepository.tambahKorban(
        token, photo, posko, kontak, name, gender, birthPlace, birthDate, momName, nik
    )
}