package com.dicoding.getpeople.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.getpeople.data.repository.UserRepository
import com.dicoding.getpeople.di.Injection
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.addVictim.AddVictimViewModel
import com.dicoding.getpeople.ui.findVictim.FindVictimViewModel
import com.dicoding.getpeople.ui.login.LoginViewModel
import com.dicoding.getpeople.ui.maps.MapsViewModel
import com.dicoding.getpeople.ui.searchResult.SearchResultViewModel
import com.dicoding.getpeople.ui.signup.SignupViewModel
import com.dicoding.getpeople.ui.welcome.WelcomeViewModel

class ViewModelFactory() :
    ViewModelProvider.NewInstanceFactory() {

    private lateinit var pref : UserPreference

    constructor(pref : UserPreference) : this() {
        this.pref = pref
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(Injection.provideUserRepository()) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref) as T
            }
            modelClass.isAssignableFrom(AddVictimViewModel::class.java) -> {
                AddVictimViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SearchResultViewModel::class.java) -> {
                SearchResultViewModel(pref) as T
            }
            modelClass.isAssignableFrom(FindVictimViewModel::class.java) -> {
                FindVictimViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

//    companion object {
//        @Volatile
//        private var instance: ViewModelFactory? = null
//        fun getInstance(context: Context): ViewModelFactory =
//            instance ?: synchronized(this) {
//                instance ?: ViewModelFactory(Injection.provideRepository(context))
//            }.also { instance = it }
//    }
}