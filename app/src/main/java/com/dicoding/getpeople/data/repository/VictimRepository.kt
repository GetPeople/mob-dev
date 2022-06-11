package com.dicoding.getpeople.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.getpeople.data.remote.response.ListKorbanResponse
import com.dicoding.getpeople.data.remote.retrofit.ApiService
import com.dicoding.getpeople.model.UserPreference
import com.dicoding.getpeople.data.Result
import com.dicoding.getpeople.data.remote.response.DefaultResponse
import retrofit2.Callback
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class VictimRepository private constructor(
    private val apiService: ApiService,
    private val preference: UserPreference
){

    private val resultListKorban = MediatorLiveData<Result<ListKorbanResponse>>()
    private val _responseListKorban = MutableLiveData<ListKorbanResponse>()
    private val responseListKorban : LiveData<ListKorbanResponse> = _responseListKorban

    private val resultTambahKorban = MediatorLiveData<Result<DefaultResponse>>()
    private val _responseTambahKorban = MutableLiveData<DefaultResponse>()
    private val responseTambahKorban : LiveData<DefaultResponse> = _responseTambahKorban

    private val resultCariKorban = MediatorLiveData<Result<ListKorbanResponse>>()
    private val _responseCariKorban = MutableLiveData<ListKorbanResponse>()
    private val responseCariKorban : LiveData<ListKorbanResponse> = _responseCariKorban

    fun getListKorban(token : String) : LiveData<Result<ListKorbanResponse>> {
        resultListKorban.value = Result.Loading
        val client = apiService.getDaftarKorban("Bearer $token")
        client.enqueue(object : Callback<ListKorbanResponse> {
            override fun onResponse(
                call: Call<ListKorbanResponse>,
                response: Response<ListKorbanResponse>
            ) {
                if (response.isSuccessful) {
                    _responseListKorban.value = response.body()
                    resultListKorban.addSource(responseListKorban) { res ->
                        resultListKorban.value = Result.Success(res)
                    }

                } else {
                    resultListKorban.value = response.body()?.let { Result.Error(it.message) }
                }
            }

            override fun onFailure(call: Call<ListKorbanResponse>, t: Throwable) {
                resultListKorban.value = t.message?.let { Result.Error(it) }
            }

        })
        return resultListKorban
    }

    fun tambahKorban(
        token: String,
        photo : MultipartBody.Part,
        posko : String,
        kontak : String,
        name : String,
        gender : String,
        birthPlace : String,
        birthDate : String,
        momName : String,
        nik : String
    ) : LiveData<Result<DefaultResponse>> {
        resultTambahKorban.value = Result.Loading
        val client = apiService.tambahKorban(
            "Bearer $token", photo, posko, kontak, name, gender, birthPlace, birthDate, momName, nik
        )
        client.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    _responseTambahKorban.value = response.body()
                    resultTambahKorban.addSource(responseTambahKorban){res ->
                        resultTambahKorban.value = Result.Success(res)
                    }
                } else {
                    resultTambahKorban.value = response.body()?.let { Result.Error(it.message) }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                resultTambahKorban.value = t.message?.let { Result.Error(it) }
            }

        })
        return resultTambahKorban
    }

    fun cariKorban(
        token: String,
        photo: MultipartBody.Part) : LiveData<Result<ListKorbanResponse>> {
        resultCariKorban.value = Result.Loading
        val client = apiService.cariKorban("Bearer $token", photo)
        client.enqueue(object : Callback<ListKorbanResponse> {
            override fun onResponse(
                call: Call<ListKorbanResponse>,
                response: Response<ListKorbanResponse>
            ) {
                if (response.isSuccessful) {
                    _responseCariKorban.value = response.body()
                    resultCariKorban.addSource(responseCariKorban) { res ->
                        resultCariKorban.value = Result.Success(res)
                    }

                } else {
                    resultCariKorban.value = response.body()?.let { Result.Error(it.message) }
                }
            }

            override fun onFailure(call: Call<ListKorbanResponse>, t: Throwable) {
                resultCariKorban.value = t.message?.let { Result.Error(it) }
            }

        })

        return resultCariKorban
    }

    companion object {
        @Volatile
        private var instance: VictimRepository? = null
        fun getInstance(
            apiService: ApiService,
            preference: UserPreference
        ) : VictimRepository =
            instance ?: synchronized(this) {
                instance ?: VictimRepository(apiService, preference)
            }.also { instance = it }
    }
}