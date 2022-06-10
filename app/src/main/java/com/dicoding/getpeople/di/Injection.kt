package com.dicoding.getpeople.di

import android.content.Context
import com.dicoding.getpeople.data.remote.retrofit.ApiConfig
import com.dicoding.getpeople.data.repository.UserRepository

object Injection {
    fun provideUserRepository() : UserRepository {
        return UserRepository.getInstance(ApiConfig.getApiService())
    }
}