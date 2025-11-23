package com.example.palmprint_recognition.data.model

/**
 * API 실패 시 공통으로 사용하는 에러 응답 모델
 *
 * @property error 에러 종류 (예: "unauthorized", "bad_request")
 * @property message 에러 상세 메시지
 * @property details 유효성 검사 실패 시 상세 내역 (optional)
 */
data class ErrorResponse(
    val error: String,
    val message: String,
    val details: List<ValidationErrorDetail>? = null
)

/**
 * 유효성 검사 실패 시의 상세 정보를 담는 모델
 *
 * @property field 문제가 발생한 필드 이름
 * @property issue 문제 내용
 */
data class ValidationErrorDetail(
    val field: String,
    val issue: String
)
