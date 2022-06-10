package com.dicoding.getpeople.model

data class UserModel(
    val name: String,
    val isLogin: Boolean,
    val id : String,
    val role: String,
    val token: String
)
