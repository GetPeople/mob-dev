package com.dicoding.getpeople.ui.detailVictim

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.launch

class DetailVictimViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}