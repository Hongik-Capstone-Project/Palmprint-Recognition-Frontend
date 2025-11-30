package com.example.palmprint_recognition.data.model

import java.io.IOException

/**
 * API 호출 실패 시 발생하는 사용자 정의 예외 클래스
 * 서버에서 반환된 에러 정보를 [errorResponse]에 담습니다.
 *
 * @param errorResponse 파싱된 에러 응답 데이터
 */
class ApiException(
    val errorResponse: ErrorResponse
) : IOException(errorResponse.message) // 예외 메시지로 서버 메시지를 사용
