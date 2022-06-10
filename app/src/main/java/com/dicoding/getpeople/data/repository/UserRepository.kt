package com.dicoding.getpeople.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.getpeople.data.remote.response.DefaultResponse
import com.dicoding.getpeople.data.remote.retrofit.ApiService
import com.dicoding.getpeople.data.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService
) {

    private val result = MediatorLiveData<Result<String>>()

    private val _message = MutableLiveData<String>()
    private val message : LiveData<String> = _message

    fun register(nama : String,
                 email : String,
                 password : String,
                 role : String,
                 idPetugas : String?)
    : LiveData<Result<String>> {
        result.value = Result.Loading
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
                    _message.value = response.body()?.message
                    result.addSource(message) { message ->
                        result.value = Result.Success(message)
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ) : UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}