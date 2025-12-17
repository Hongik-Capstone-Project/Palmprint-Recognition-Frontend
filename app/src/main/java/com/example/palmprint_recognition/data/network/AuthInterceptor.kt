package com.example.palmprint_recognition.data.network

import com.example.palmprint_recognition.data.local.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 모든 API 요청에 Authorization 헤더를 자동으로 붙여주는 인터셉터
 *
 * - access token이 있으면: Authorization: Bearer <token>
 * - 없으면: 아무것도 안 하고 그대로 통과
 */
class AuthInterceptor @Inject constructor(
    private val prefs: PreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = prefs.getAccessToken()

        // 토큰이 없으면 그대로 진행
        if (accessToken.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}
