package com.dicoding.getpeople.data.remote.retrofit

import com.dicoding.getpeople.data.remote.request.UserRequest
import com.dicoding.getpeople.data.remote.response.LoginResponse
import com.dicoding.getpeople.data.remote.response.DefaultResponse
import com.dicoding.getpeople.data.remote.response.ListKorbanResponse
import okhttp3.MultipartBody
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
    @POST("korban/cari")
    fun cariKorban(
        @Header("Authorization") authHeader : String,
        @Part file: MultipartBody.Part
    ) : Call<ListKorbanResponse>

    @Multipart
    @FormUrlEncoded
    @POST("victim/add")
    fun tambahKorban(
        @Header("Authorization") authHeader : String,
        @Part file: MultipartBody.Part,
        @Field("posko") posko : String,
        @Field("kontak") kontak : String,
        @Field("name") name : String,
        @Field("gender") gender : String,
        @Field("birthPlace") birthPlace: String,
        @Field("birthDate") birthDate : String,
        @Field("momName") momName : String,
        @Field("nik") nik : String,
    ) : Call<DefaultResponse>
}