package com.dicoding.getpeople.ui.detailVictim

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.launch

class DetailVictimViewModel(private val pref: UserPreference) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}