package com.example.palmprint_recognition.data.network

import com.example.palmprint_recognition.data.api.AdminApi
import com.example.palmprint_recognition.data.api.AuthApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit 클라이언트 인스턴스를 관리하는 싱글톤 객체
 */
object RetrofitInstance {

    // API 서버의 기본 URL
    // 10.0.2.2는 Android 에뮬레이터에서 호스트 PC의 localhost를 가리키는 주소입니다.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // JSON 파싱을 위한 Gson 객체
    // 외부(Repository)에서 에러 바디를 파싱할 때 재사용하기 위해 public으로 설정
    val gson: Gson = GsonBuilder()
        .create()

    // Retrofit 클라이언트 인스턴스
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)) // 생성한 Gson 객체 사용
            .build()
    }

    /**
     * AuthApi 인터페이스의 구현체를 반환합니다.
     */
    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    /**
     * AdminApi 인터페이스의 구현체를 반환합니다.
     */
    val adminApi: AdminApi by lazy {
        retrofit.create(AdminApi::class.java)
    }

}