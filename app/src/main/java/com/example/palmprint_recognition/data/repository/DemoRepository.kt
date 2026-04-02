package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.model.DemoRegisterResponse
import com.example.palmprint_recognition.data.model.DemoVerifyResponse

/**
 * 데모 관련 기능에 대한 Repository 인터페이스
 */
interface DemoRepository {

    /**
     * 데모 장문 등록
     */
    suspend fun registerDemoPalmprint(
        name: String,
        palmprintData: String
    ): DemoRegisterResponse

    /**
     * 데모 장문 검증
     */
    suspend fun verifyDemoPalmprint(
        palmprintData: String
    ): DemoVerifyResponse
}