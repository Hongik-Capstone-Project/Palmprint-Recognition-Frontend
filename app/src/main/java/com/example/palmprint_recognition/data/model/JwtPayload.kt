package com.example.palmprint_recognition.data.model

import com.google.gson.annotations.SerializedName

data class JwtPayload(
    val sub: String,
    val exp: Long,
    val type: String,
    val user: JwtUser
)

data class JwtUser(
    val id: String,
    val email: String,
    val name: String,

    /**
     * 서버에서 "admin" / "user" 형태로 옴
     */
    val role: String
)


