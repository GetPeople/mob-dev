package com.dicoding.getpeople.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?:"",
                preferences[STATE_KEY] ?: false,
                preferences[ID_KEY] ?: "",
                preferences[ROLE_KEY] ?: "",
                preferences[TOKEN_KEY] ?: ""
            )
        }
    }

    fun getToken() : Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY].toString()
        }
    }

    suspend fun login(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[ID_KEY] = user.id
            preferences[STATE_KEY] = user.isLogin
            preferences[ROLE_KEY] = user.role
            preferences[TOKEN_KEY] = user.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val ID_KEY = stringPreferencesKey("id")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}