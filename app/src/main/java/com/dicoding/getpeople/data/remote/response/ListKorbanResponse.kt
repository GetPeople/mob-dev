package com.dicoding.getpeople.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListKorbanResponse(

	@field:SerializedName("listKorban")
	val listKorban: List<KorbanItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class KorbanItem(

	@field:SerializedName("momName")
	val momName: String? = null,

	@field:SerializedName("image")
	val photoUrl: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("birthPlace")
	val birthPlace: String? = null,

	@field:SerializedName("contact")
	val contact: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("posko")
	val posko: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null
) : Parcelable
