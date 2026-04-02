package com.example.palmprint_recognition.data.model

import com.google.gson.annotations.SerializedName

/**
 * POST /api/demo/register 요청 바디
 * {
 *   "name": "홍길동",
 *   "palmprint_data": "base64..."
 * }
 */
data class DemoRegisterRequest(
    val name: String,
    @SerializedName("palmprint_data")
    val palmprintData: String
)

/**
 * POST /api/demo/register 응답
 * {
 *   "user_id": 1,
 *   "palm_id": 10,
 *   "name": "홍길동",
 *   "message": "..."
 * }
 */
data class DemoRegisterResponse(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("palm_id")
    val palmId: Int,

    val name: String,
    val message: String
)

/**
 * POST /api/demo/verify 요청 바디
 * {
 *   "palmprint_data": "base64..."
 * }
 */
data class DemoVerifyRequest(
    @SerializedName("palmprint_data")
    val palmprintData: String
)

/**
 * POST /api/demo/verify 응답
 * {
 *   "matched": true,
 *   "name": "홍*동",
 *   "similarity_score": 0.92
 * }
 */
data class DemoVerifyResponse(
    val matched: Boolean,

    // 매칭 실패 시 null 가능성 고려
    val name: String? = null,

    @SerializedName("similarity_score")
    val similarityScore: Double? = null
)