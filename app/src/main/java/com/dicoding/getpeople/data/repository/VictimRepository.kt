package com.dicoding.getpeople.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.getpeople.data.remote.response.ListKorbanResponse
import com.dicoding.getpeople.data.remote.retrofit.ApiService
import com.dicoding.getpeople.data.Result
import com.dicoding.getpeople.data.remote.response.DefaultResponse
import retrofit2.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File

class VictimRepository private constructor(
    private val apiService: ApiService
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
                    resultListKorban.removeSource(responseListKorban)

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        resultListKorban.value = Result.Error(jObjError.getString("message"))
                    } catch (e: Exception) {

                    }
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
        image : File,
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
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", image.name, requestImageFile)
            .addFormDataPart("posko", posko)
            .addFormDataPart("contact", kontak)
            .addFormDataPart("name", name)
            .addFormDataPart("gender", gender)
            .addFormDataPart("birthPlace", birthPlace)
            .addFormDataPart("birthDate", birthDate)
            .addFormDataPart("momName", momName)
            .addFormDataPart("nik", nik)
            .build()
        val client = apiService.tambahKorban(
            "Bearer $token",
            body
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
                    resultTambahKorban.removeSource(responseTambahKorban)
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        resultTambahKorban.value = Result.Error(jObjError.getString("message"))
                    } catch (e: Exception) {

                    }
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
                    resultCariKorban.removeSource(responseCariKorban)

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        resultCariKorban.value = Result.Error(jObjError.getString("message"))
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFailure(call: Call<ListKorbanResponse>, t: Throwable) {
                resultCariKorban.value = t.message?.let { Result.Error(it) }
            }

        })
        return resultCariKorban
    }

    private fun toRequestBody(param: String) = param.toRequestBody("text/plain".toMediaType())

    companion object {
        @Volatile
        private var instance: VictimRepository? = null
        fun getInstance(
            apiService: ApiService
        ) : VictimRepository =
            instance ?: synchronized(this) {
                instance ?: VictimRepository(apiService)
            }.also { instance = it }
    }
}