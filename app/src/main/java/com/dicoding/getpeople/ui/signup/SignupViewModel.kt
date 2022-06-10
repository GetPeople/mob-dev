package com.dicoding.getpeople.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.data.repository.UserRepository
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.launch

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(nama : String,
                 email : String,
                 password : String,
                 role : String,
                 idPetugas : String?)
    = userRepository.register(nama, email, password, role, idPetugas)
}