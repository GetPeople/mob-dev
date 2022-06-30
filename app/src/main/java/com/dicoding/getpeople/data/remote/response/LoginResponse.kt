package com.dicoding.getpeople.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(

	@field:SerializedName("data")
	val loginResult: LoginResult? = null,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class LoginResult(

	@field:SerializedName("idUser")
	val idUser: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("accessToken")
	val token: String
) : Parcelable
