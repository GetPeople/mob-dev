package com.dicoding.getpeople.data.remote.retrofit

import com.dicoding.getpeople.data.remote.request.UserRequest
import com.dicoding.getpeople.data.remote.response.LoginResponse
import com.dicoding.getpeople.data.remote.response.DefaultResponse
import com.dicoding.getpeople.data.remote.response.ListKorbanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(
        @Body userRequest: UserRequest
    ) : Call<DefaultResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(
        @Body userRequest: UserRequest
    ) : Call<LoginResponse>

    @GET("victim/list")
    fun getDaftarKorban(
        @Header("Authorization") authHeader : String
    ) : Call<ListKorbanResponse>

    @Multipart
    @POST("victim/find")
    fun cariKorban(
        @Header("Authorization") authHeader : String,
        @Part file: MultipartBody.Part
    ) : Call<ListKorbanResponse>

    @POST("victim/upload")
    fun tambahKorban(
        @Header("Authorization") authHeader : String,
        @Body body: MultipartBody
    ) : Call<DefaultResponse>
}