package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.model.PalmprintRegistrationStatusResponse
import com.example.palmprint_recognition.data.model.RegisterPalmprintResponse
import com.example.palmprint_recognition.data.model.Institution
import com.example.palmprint_recognition.data.model.PaymentMethod
import com.example.palmprint_recognition.data.model.AddPaymentMethodRequest
import com.example.palmprint_recognition.data.model.UserInstitution
import com.example.palmprint_recognition.data.model.PagedResponse
import com.example.palmprint_recognition.data.model.ReportResponse
import com.example.palmprint_recognition.data.model.UserVerificationLog
import com.example.palmprint_recognition.data.model.ReportVerificationRequest

/**
 * 사용자 관련 기능에 대한 Repository 인터페이스 (설계도)
 */
interface UserRepository {
    suspend fun getUserInstitutions(): List<UserInstitution>

    suspend fun addUserInstitution(
        institutionId: Int,
        institutionUserId: String
    ): UserInstitution

    suspend fun deleteUserInstitution(institutionId: Int)

    suspend fun deleteMe()

    suspend fun getPaymentMethods(): List<PaymentMethod>

    suspend fun addPaymentMethod(cardName: String, cardId: String): PaymentMethod

    suspend fun deletePaymentMethod(paymentMethodId: Int)

    suspend fun getPalmprintRegistrationStatus(): PalmprintRegistrationStatusResponse

    /**
     * @param palmprintData Base64 string
     * @param userId 서버가 user_id를 요구할 때만 사용 (그 외 null)
     */
    suspend fun registerPalmprint(palmprintData: String, userId: Int? = null): RegisterPalmprintResponse

    suspend fun deletePalmprint()

    suspend fun getUserVerifications(page: Int, size: Int): PagedResponse<UserVerificationLog>

    suspend fun reportVerification(logId: String, reason: String): ReportResponse
}