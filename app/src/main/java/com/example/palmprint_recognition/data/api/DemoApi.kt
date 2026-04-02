package com.example.palmprint_recognition.data.api

import com.example.palmprint_recognition.data.model.DemoRegisterRequest
import com.example.palmprint_recognition.data.model.DemoRegisterResponse
import com.example.palmprint_recognition.data.model.DemoVerifyRequest
import com.example.palmprint_recognition.data.model.DemoVerifyResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 데모 전용 REST API 인터페이스
 * - 인증 토큰 없이 호출
 */
interface DemoApi {

    /**
     * 데모 장문 등록
     * POST /api/demo/register
     */
    @POST("/api/demo/register")
    suspend fun registerDemoPalmprint(
        @Body request: DemoRegisterRequest
    ): DemoRegisterResponse

    /**
     * 데모 장문 검증
     * POST /api/demo/verify
     */
    @POST("/api/demo/verify")
    suspend fun verifyDemoPalmprint(
        @Body request: DemoVerifyRequest
    ): DemoVerifyResponse
}