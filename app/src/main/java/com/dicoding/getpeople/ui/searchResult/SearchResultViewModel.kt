package com.dicoding.getpeople.ui.searchResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.launch

class SearchResultViewModel(private val pref: UserPreference) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}