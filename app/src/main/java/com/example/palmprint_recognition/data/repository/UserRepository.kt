package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.model.AddPaymentMethodResponse
import com.example.palmprint_recognition.data.model.AddUserInstitutionResponse
import com.example.palmprint_recognition.data.model.PalmprintRegistrationStatusResponse
import com.example.palmprint_recognition.data.model.PaymentMethodsResponse
import com.example.palmprint_recognition.data.model.RegisterPalmprintResponse
import com.example.palmprint_recognition.data.model.UserInstitutionsResponse
import com.example.palmprint_recognition.data.model.UserVerificationsResponse

/**
 * 사용자 관련 기능에 대한 Repository 인터페이스 (설계도)
 */
interface UserRepository {
    suspend fun getUserInstitutions(): UserInstitutionsResponse

    suspend fun addUserInstitution(institutionName: String, institutionUserId: String): AddUserInstitutionResponse

    suspend fun deleteUserInstitution(institutionId: String)

    suspend fun getPaymentMethods(): PaymentMethodsResponse

    suspend fun addPaymentMethod(cardName: String, cardId: String): AddPaymentMethodResponse

    suspend fun deletePaymentMethod(paymentMethodId: String)

    suspend fun getPalmprintRegistrationStatus(): PalmprintRegistrationStatusResponse

    suspend fun registerPalmprint(palmprintData: String): RegisterPalmprintResponse

    suspend fun deletePalmprint()

    suspend fun getUserVerifications(): UserVerificationsResponse

    suspend fun reportVerification(logId: String, reason: String)
}