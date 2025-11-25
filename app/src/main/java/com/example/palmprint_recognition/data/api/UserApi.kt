package com.example.palmprint_recognition.data.api

import com.example.palmprint_recognition.data.model.AddPaymentMethodRequest
import com.example.palmprint_recognition.data.model.AddPaymentMethodResponse
import com.example.palmprint_recognition.data.model.AddUserInstitutionRequest
import com.example.palmprint_recognition.data.model.AddUserInstitutionResponse
import com.example.palmprint_recognition.data.model.PalmprintRegistrationStatusResponse
import com.example.palmprint_recognition.data.model.PaymentMethodsResponse
import com.example.palmprint_recognition.data.model.RegisterPalmprintRequest
import com.example.palmprint_recognition.data.model.RegisterPalmprintResponse
import com.example.palmprint_recognition.data.model.ReportVerificationRequest
import com.example.palmprint_recognition.data.model.UserInstitutionsResponse
import com.example.palmprint_recognition.data.model.UserVerificationsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 사용자 관련 REST API 인터페이스
 */
interface UserApi {

    /**
     * 현재 로그인된 사용자의 등록된 기관 목록을 조회하는 API
     *
     * @return 사용자의 기관 목록과 페이지 정보
     */
    @GET("/api/users/me/institutions")
    suspend fun getUserInstitutions(): UserInstitutionsResponse

    /**
     * 현재 로그인된 사용자에게 새로운 기관을 추가하는 API
     *
     * @param request 추가할 기관 정보
     * @return 등록된 기관 정보와 성공 상태
     */
    @POST("/api/users/me/institutions")
    suspend fun addUserInstitution(
        @Body request: AddUserInstitutionRequest
    ): AddUserInstitutionResponse

    /**
     * 현재 로그인된 사용자의 특정 기관 연결을 삭제하는 API
     *
     * @param institutionId 삭제할 기관의 ID
     */
    @DELETE("/api/users/me/institutions/{institution_id}")
    suspend fun deleteUserInstitution(
        @Path("institution_id") institutionId: String
    ): Unit

    /**
     * 현재 로그인된 사용자의 등록된 결제 수단 목록을 조회하는 API
     *
     * @return 사용자의 결제 수단 목록과 페이지 정보
     */
    @GET("/api/users/me/payment_methods")
    suspend fun getPaymentMethods(): PaymentMethodsResponse

    /**
     * 현재 로그인된 사용자에게 새로운 결제 수단을 추가하는 API
     *
     * @param request 추가할 결제 수단 정보
     * @return 등록된 결제 수단 정보와 성공 상태
     */
    @POST("/api/users/me/payment_methods")
    suspend fun addPaymentMethod(
        @Body request: AddPaymentMethodRequest
    ): AddPaymentMethodResponse

    /**
     * 현재 로그인된 사용자의 특정 결제 수단을 삭제하는 API
     *
     * @param paymentMethodId 삭제할 결제 수단의 ID
     */
    @DELETE("/api/users/me/payment_methods/{payment_method_id}")
    suspend fun deletePaymentMethod(
        @Path("payment_method_id") paymentMethodId: String
    ): Unit

    /**
     * 현재 로그인된 사용자의 손바닥 등록 여부를 조회하는 API
     *
     * @return 사용자의 손바닥 등록 여부
     */
    @GET("/api/users/me/palmprints")
    suspend fun getPalmprintRegistrationStatus(): PalmprintRegistrationStatusResponse

    /**
     * 현재 로그인된 사용자의 손바닥 정보를 등록하는 API
     *
     * @param request 등록할 손바닥 데이터
     * @return 등록된 손바닥 정보와 성공 메시지
     */
    @POST("/api/users/me/palmprints")
    suspend fun registerPalmprint(
        @Body request: RegisterPalmprintRequest
    ): RegisterPalmprintResponse

    /**
     * 현재 로그인된 사용자의 손바닥 정보를 삭제하는 API
     */
    @DELETE("/api/users/me/palmprints")
    suspend fun deletePalmprint(): Unit

    /**
     * 현재 로그인된 사용자의 인증 내역을 조회하는 API
     *
     * @return 사용자의 인증 내역과 페이지 정보
     */
    @GET("/api/users/me/verifications")
    suspend fun getUserVerifications(): UserVerificationsResponse

    /**
     * 특정 인증 내역을 신고하는 API
     *
     * @param logId 신고할 인증 로그의 ID
     * @param request 신고 사유
     */
    @POST("/api/users/me/verifications/{log_id}/report")
    suspend fun reportVerification(
        @Path("log_id") logId: String,
        @Body request: ReportVerificationRequest
    ): Unit

}
