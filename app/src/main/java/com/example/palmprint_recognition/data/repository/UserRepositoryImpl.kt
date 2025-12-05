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
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val gson: Gson
) : UserRepository {

    private fun parseError(e: HttpException): ApiException {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            ApiException(errorResponse)
        } catch (jsonException: Exception) {
            val errorMessage = e.response()?.message() ?: "An unknown error occurred"
            ApiException(ErrorResponse("parse_error", errorMessage))
        }
    }

    override suspend fun getUserInstitutions(): UserInstitutionsResponse {
        return try {
            userApi.getUserInstitutions()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun addUserInstitution(institutionName: String, institutionUserId: String): AddUserInstitutionResponse {
        return try {
            val request = AddUserInstitutionRequest(institutionName, institutionUserId)
            userApi.addUserInstitution(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun deleteUserInstitution(institutionId: String) {
        try {
            userApi.deleteUserInstitution(institutionId)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getPaymentMethods(): PaymentMethodsResponse {
        return try {
            userApi.getPaymentMethods()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun addPaymentMethod(cardName: String, cardId: String): AddPaymentMethodResponse {
        return try {
            val request = AddPaymentMethodRequest(cardName, cardId)
            userApi.addPaymentMethod(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun deletePaymentMethod(paymentMethodId: String) {
        try {
            userApi.deletePaymentMethod(paymentMethodId)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getPalmprintRegistrationStatus(): PalmprintRegistrationStatusResponse {
        return try {
            userApi.getPalmprintRegistrationStatus()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun registerPalmprint(palmprintData: String): RegisterPalmprintResponse {
        return try {
            val request = RegisterPalmprintRequest(palmprintData)
            userApi.registerPalmprint(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun deletePalmprint() {
        try {
            userApi.deletePalmprint()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getUserVerifications(): UserVerificationsResponse {
        return try {
            userApi.getUserVerifications()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun reportVerification(logId: String, reason: String) {
        try {
            val request = ReportVerificationRequest(reason)
            userApi.reportVerification(logId, request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }
}