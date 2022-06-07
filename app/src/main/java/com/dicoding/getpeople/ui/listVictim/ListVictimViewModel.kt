package com.dicoding.getpeople.ui.listVictim

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.launch

class ListVictimViewModel(private val pref: UserPreference) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}