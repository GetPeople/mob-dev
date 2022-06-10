package com.dicoding.getpeople.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DefaultResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String
) : Parcelable
