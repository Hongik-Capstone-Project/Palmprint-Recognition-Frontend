package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.api.UserApi
import com.example.palmprint_recognition.data.model.AddPaymentMethodRequest
import com.example.palmprint_recognition.data.model.AddPaymentMethodResponse
import com.example.palmprint_recognition.data.model.AddUserInstitutionRequest
import com.example.palmprint_recognition.data.model.AddUserInstitutionResponse
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.model.ErrorResponse
import com.example.palmprint_recognition.data.model.PalmprintRegistrationStatusResponse
import com.example.palmprint_recognition.data.model.PaymentMethodsResponse
import com.example.palmprint_recognition.data.model.RegisterPalmprintRequest
import com.example.palmprint_recognition.data.model.RegisterPalmprintResponse
import com.example.palmprint_recognition.data.model.ReportVerificationRequest
import com.example.palmprint_recognition.data.model.UserInstitutionsResponse
import com.example.palmprint_recognition.data.model.UserVerificationsResponse
import com.example.palmprint_recognition.data.network.RetrofitInstance
import retrofit2.HttpException

/**
 * 사용자 관련 API 호출을 담당하는 Repository
 * API 호출 시 발생하는 예외를 [ApiException]으로 변환하여 throw 합니다.
 */
class UserRepository(
    private val userApi: UserApi
) {

    /**
     * 현재 로그인된 사용자의 등록된 기관 목록을 조회하는 API를 호출한다.
     * 성공 시 [UserInstitutionsResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @return 서버에서 받은 사용자의 기관 목록과 페이지 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getUserInstitutions(): UserInstitutionsResponse {
        return try {
            userApi.getUserInstitutions()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자에게 새로운 기관을 추가하는 API를 호출한다.
     * 성공 시 [AddUserInstitutionResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param institutionName 기관 이름
     * @param institutionUserId 해당 기관에서의 사용자 ID
     * @return 서버에서 받은 등록된 기관 정보와 성공 상태
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun addUserInstitution(institutionName: String, institutionUserId: String): AddUserInstitutionResponse {
        return try {
            val request = AddUserInstitutionRequest(institutionName, institutionUserId)
            userApi.addUserInstitution(request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자의 특정 기관 연결을 삭제하는 API를 호출한다.
     * 성공 시 Unit을 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param institutionId 삭제할 기관의 ID
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun deleteUserInstitution(institutionId: String) {
        try {
            userApi.deleteUserInstitution(institutionId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자의 등록된 결제 수단 목록을 조회하는 API를 호출한다.
     * 성공 시 [PaymentMethodsResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @return 서버에서 받은 사용자의 결제 수단 목록과 페이지 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getPaymentMethods(): PaymentMethodsResponse {
        return try {
            userApi.getPaymentMethods()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자에게 새로운 결제 수단을 추가하는 API를 호출한다.
     * 성공 시 [AddPaymentMethodResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param cardName 카드 이름
     * @param cardId 카드 식별자
     * @return 서버에서 받은 등록된 결제 수단 정보와 성공 상태
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun addPaymentMethod(cardName: String, cardId: String): AddPaymentMethodResponse {
        return try {
            val request = AddPaymentMethodRequest(cardName, cardId)
            userApi.addPaymentMethod(request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자의 특정 결제 수단을 삭제하는 API를 호출한다.
     * 성공 시 Unit을 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param paymentMethodId 삭제할 결제 수단의 ID
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun deletePaymentMethod(paymentMethodId: String) {
        try {
            userApi.deletePaymentMethod(paymentMethodId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자의 손바닥 등록 여부를 조회하는 API를 호출한다.
     * 성공 시 [PalmprintRegistrationStatusResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @return 서버에서 받은 사용자의 손바닥 등록 여부
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getPalmprintRegistrationStatus(): PalmprintRegistrationStatusResponse {
        return try {
            userApi.getPalmprintRegistrationStatus()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자의 손바닥 정보를 등록하는 API를 호출한다.
     * 성공 시 [RegisterPalmprintResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param palmprintData Base64 인코딩된 손바닥 데이터
     * @return 서버에서 받은 등록된 손바닥 정보와 성공 메시지
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun registerPalmprint(palmprintData: String): RegisterPalmprintResponse {
        return try {
            val request = RegisterPalmprintRequest(palmprintData)
            userApi.registerPalmprint(request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자의 손바닥 정보를 삭제하는 API를 호출한다.
     * 성공 시 Unit을 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun deletePalmprint() {
        try {
            userApi.deletePalmprint()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 현재 로그인된 사용자의 인증 내역을 조회하는 API를 호출한다.
     * 성공 시 [UserVerificationsResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @return 서버에서 받은 사용자의 인증 내역과 페이지 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getUserVerifications(): UserVerificationsResponse {
        return try {
            userApi.getUserVerifications()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 인증 내역을 신고하는 API를 호출한다.
     * 성공 시 Unit을 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param logId 신고할 인증 로그의 ID
     * @param reason 신고 사유
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun reportVerification(logId: String, reason: String) {
        try {
            val request = ReportVerificationRequest(reason)
            userApi.reportVerification(logId, request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = RetrofitInstance.gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

}
