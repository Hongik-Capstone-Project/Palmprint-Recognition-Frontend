package com.example.palmprint_recognition.data.api

import com.example.palmprint_recognition.data.model.AddUserInstitutionRequest
import com.example.palmprint_recognition.data.model.RegisterPalmprintRequest
import com.example.palmprint_recognition.data.model.RegisterPalmprintResponse
import com.example.palmprint_recognition.data.model.UserInstitution
import com.example.palmprint_recognition.data.model.PagedResponse
import com.example.palmprint_recognition.data.model.ReportResponse
import com.example.palmprint_recognition.data.model.UserVerificationLog
import com.example.palmprint_recognition.data.model.ReportVerificationRequest
import com.example.palmprint_recognition.data.model.PaymentMethod
import com.example.palmprint_recognition.data.model.AddPaymentMethodRequest
import com.example.palmprint_recognition.data.model.GetMyPalmsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 사용자 관련 REST API 인터페이스
 */
interface UserApi {


    /**
     * 현재 로그인된 사용자의 등록된 기관 목록 조회
     * GET /api/users/me/institutions
     * Response: List<UserInstitution>
     */
    @GET("/api/users/me/institutions")
    suspend fun getUserInstitutions(): List<UserInstitution>

    /**
     * 현재 로그인된 사용자에게 기관 추가
     * POST /api/users/me/institutions
     * Response: UserInstitution
     */
    @POST("/api/users/me/institutions")
    suspend fun addUserInstitution(
        @Body request: AddUserInstitutionRequest
    ): UserInstitution

    /**
     * 현재 로그인된 사용자의 특정 기관 연결 삭제
     * DELETE /api/users/me/institutions/{institution_id}
     * Response: 204 (Unit)
     */
    @DELETE("/api/users/me/institutions/{institution_id}")
    suspend fun deleteUserInstitution(
        @Path("institution_id") institutionId: Int
    ): Unit

    /**
     * 회원탈퇴 (현재 로그인한 사용자 삭제)
     * DELETE /api/users/me
     * Response: 204 (Unit)
     */
    @DELETE("/api/users/me")
    suspend fun deleteMe(): Unit

    /**
     * 결제 수단 리스트 조회
     * GET /api/users/me/payment_methods
     * Response: List<PaymentMethod>
     */
    @GET("/api/users/me/payment_methods")
    suspend fun getPaymentMethods(): List<PaymentMethod>

    /**
     * 결제 수단 추가
     * POST /api/users/me/payment_methods
     * Response: PaymentMethod
     */
    @POST("/api/users/me/payment_methods")
    suspend fun addPaymentMethod(
        @Body request: AddPaymentMethodRequest
    ): PaymentMethod

    /**
     * 결제 수단 삭제
     * DELETE /api/users/me/payment_methods/{payment_method_id}
     * Response: 204 (Unit)
     */
    @DELETE("/api/users/me/payment_methods/{payment_method_id}")
    suspend fun deletePaymentMethod(
        @Path("payment_method_id") paymentMethodId: Int
    ): Unit

    /**
     * 1) Get My Palms
     * GET /api/users/me/palmprints
     */
    @GET("/api/users/me/palmprints")
    suspend fun getMyPalms(): GetMyPalmsResponse

    /**
     * 2) Register Palm
     * POST /api/users/me/palmprints
     */
    @POST("/api/users/me/palmprints")
    suspend fun registerPalmprint(
        @Body request: RegisterPalmprintRequest
    ): RegisterPalmprintResponse

    /**
     * 3) Delete All My Palms
     * DELETE /api/users/me/palmprints/me/all
     */
    @DELETE("/api/users/me/palmprints/me/all")
    suspend fun deleteAllMyPalms(): Unit

    /**
     * 4) Delete Palm
     * DELETE /api/users/me/palmprints/me/{palm_id}
     */
    @DELETE("/api/users/me/palmprints/me/{palm_id}")
    suspend fun deletePalm(
        @Path("palm_id") palmId: Int
    ): Unit

    /**
     * 내 인증(검증) 내역 조회 (페이지네이션)
     * GET /api/users/me/verifications?page=1&size=50
     */
    @GET("/api/users/me/verifications")
    suspend fun getUserVerifications(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PagedResponse<UserVerificationLog>

    /**
     * 인증 내역 신고
     * POST /api/users/me/verifications/{log_id}/report
     */
    @POST("/api/users/me/verifications/{log_id}/report")
    suspend fun reportVerification(
        @Path("log_id") logId: String,
        @Body request: ReportVerificationRequest
    ): ReportResponse

}
