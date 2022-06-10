package com.dicoding.getpeople.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListKorbanResponse(

	@field:SerializedName("listKorban")
	val listKorban: List<ListKorbanItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class ListKorbanItem(

	@field:SerializedName("momName")
	val momName: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("birthPlace")
	val birthPlace: String? = null,

	@field:SerializedName("kontak")
	val kontak: String? = null,

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
