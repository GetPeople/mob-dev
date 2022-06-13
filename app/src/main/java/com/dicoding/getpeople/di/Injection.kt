package com.dicoding.getpeople.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.getpeople.data.remote.retrofit.ApiConfig
import com.dicoding.getpeople.data.repository.UserRepository
import com.dicoding.getpeople.data.repository.VictimRepository
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.welcome.dataStore


object Injection {
    private val apiService = ApiConfig.getApiService()
    fun provideUserRepository(preference: UserPreference) : UserRepository {
        return UserRepository.getInstance(apiService, preference)
    }

    fun provideVictimRepository() : VictimRepository {
        return VictimRepository.getInstance(apiService)
    }
}