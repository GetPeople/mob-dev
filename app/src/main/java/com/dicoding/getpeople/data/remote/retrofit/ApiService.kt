package com.dicoding.getpeople.data.remote.retrofit

import com.dicoding.getpeople.data.remote.response.LoginResponse
import com.dicoding.getpeople.data.remote.response.DefaultResponse
import com.dicoding.getpeople.data.remote.response.ListKorbanResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String,
        @Field("idPetugas") idPetugas: String?
    ) : Call<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @DELETE("logout")
    fun logout(
    ) : Call<DefaultResponse>

    @GET("korban/daftar")
    fun getDaftarKorban(
        @Header("Authorization") authHeader : String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : Call<ListKorbanResponse>

    @Multipart
    @POST("korban/cari")
    fun cariKorban(
        @Header("Authorization") authHeader : String,
        @Part file: MultipartBody.Part
    ) : Call<ListKorbanResponse>

    @Multipart
    @FormUrlEncoded
    @POST("korban/tambah")
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