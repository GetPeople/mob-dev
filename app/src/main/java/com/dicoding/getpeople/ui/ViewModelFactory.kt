package com.dicoding.getpeople.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.di.Injection
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.addVictim.AddVictimViewModel
import com.dicoding.getpeople.ui.detailVictim.DetailVictimViewModel
import com.dicoding.getpeople.ui.findVictim.FindVictimViewModel
import com.dicoding.getpeople.ui.listVictim.ListVictimViewModel
import com.dicoding.getpeople.ui.loading.LoadingViewModel
import com.dicoding.getpeople.ui.login.LoginViewModel
import com.dicoding.getpeople.ui.maps.MapsViewModel
import com.dicoding.getpeople.ui.searchResult.SearchResultViewModel
import com.dicoding.getpeople.ui.signup.SignupViewModel
import com.dicoding.getpeople.ui.welcome.WelcomeViewModel

class ViewModelFactory private constructor(private val pref: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(Injection.provideUserRepository(pref)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideUserRepository(pref)) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref) as T
            }
            modelClass.isAssignableFrom(AddVictimViewModel::class.java) -> {
                AddVictimViewModel(pref, Injection.provideVictimRepository()) as T
            }
            modelClass.isAssignableFrom(SearchResultViewModel::class.java) -> {
                SearchResultViewModel(pref) as T
            }
            modelClass.isAssignableFrom(FindVictimViewModel::class.java) -> {
                FindVictimViewModel(pref, Injection.provideVictimRepository()) as T
            }
            modelClass.isAssignableFrom(ListVictimViewModel::class.java) -> {
                ListVictimViewModel(pref, Injection.provideVictimRepository()) as T
            }
            modelClass.isAssignableFrom(LoadingViewModel::class.java) -> {
                LoadingViewModel(pref, Injection.provideVictimRepository()) as T
            }
            modelClass.isAssignableFrom(DetailVictimViewModel::class.java) -> {
                DetailVictimViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(pref: UserPreference): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(pref)
            }.also { instance = it }
    }
}