package com.example.palmprint_recognition.data.local

import android.util.Base64
import com.google.gson.Gson
import java.lang.IllegalArgumentException

object JwtUtils {

    val gson = Gson()

    /**
     * JWT access token에서 payload(JSON 문자열) 추출
     */
    fun decodePayload(token: String): String {
        val parts = token.split(".")
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid JWT token")
        }

        val payloadBase64 = parts[1]
        val decodedBytes = Base64.decode(
            payloadBase64,
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
        return String(decodedBytes, Charsets.UTF_8)
    }

    /**
     * JWT payload를 특정 모델로 변환
     */
    inline fun <reified T> decodePayloadAs(token: String): T {
        val json = decodePayload(token)
        return gson.fromJson(json, T::class.java)
    }

    fun normalizeRole(serverRole: String): String {
        return if (serverRole.lowercase() == "admin") {
            "ADMIN"
        } else {
            "USER"
        }
    }

}
