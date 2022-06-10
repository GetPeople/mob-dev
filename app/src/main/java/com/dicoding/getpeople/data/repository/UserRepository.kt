package com.dicoding.getpeople.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.getpeople.data.remote.response.DefaultResponse
import com.dicoding.getpeople.data.remote.retrofit.ApiService
import com.dicoding.getpeople.data.Result
import com.dicoding.getpeople.data.remote.response.LoginResponse
import com.dicoding.getpeople.model.UserModel
import com.dicoding.getpeople.model.UserPreference
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val preference: UserPreference
) {

    private val resultRegister = MediatorLiveData<Result<DefaultResponse>>()
    private val resultLogin = MediatorLiveData<Result<LoginResponse>>()

    private val _responseRegister = MutableLiveData<DefaultResponse>()
    private val responseRegister : LiveData<DefaultResponse> = _responseRegister

    private val _responseLogin = MutableLiveData<LoginResponse>()
    private val responseLogin : LiveData<LoginResponse> = _responseLogin

    fun register(nama : String,
                 email : String,
                 password : String,
                 role : String,
                 idPetugas : String?)
    : LiveData<Result<DefaultResponse>> {
        resultRegister.value = Result.Loading
        val client = if (idPetugas == null) {
            apiService.register(nama, email, password, role, null)
        } else {
            apiService.register(nama, email, password, role, idPetugas)
        }
        client.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    _responseRegister.value = response.body()
                    resultRegister.addSource(responseRegister) { resp ->
                        resultRegister.value = Result.Success(resp)
                    }
                } else {
                    resultRegister.value = response.body()?.let { Result.Error(it.message) }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                resultRegister.value = t.message?.let { Result.Error(it) }
            }

        })
        return resultRegister
    }

    fun login(
        email: String,
        password: String
    ) : LiveData<Result<LoginResponse>> {
        resultLogin.value = Result.Loading
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResult = response.body()?.loginResult
                    _responseLogin.value = response.body()
                    val user = loginResult?.let {
                        UserModel(it.name, true, it.idUser, it.role, it.token)
                    }
                    if (user != null) {
                        runBlocking {
                            preference.login(user)
                        }
                    }
                    resultLogin.addSource(responseLogin) { resp ->
                        resultLogin.value = Result.Success(resp)
                    }
                } else {
                    resultLogin.value = response.body()?.message?.let { Result.Error(it) }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                resultLogin.value = t.message?.let { Result.Error(it) }
            }

        })
        return resultLogin
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            preference: UserPreference
        ) : UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, preference)
            }.also { instance = it }
    }
}