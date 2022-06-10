package com.dicoding.getpeople.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.getpeople.data.remote.retrofit.ApiConfig
import com.dicoding.getpeople.data.repository.UserRepository
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.ui.welcome.dataStore


object Injection {
    fun provideUserRepository(preference: UserPreference) : UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, preference)
    }
}