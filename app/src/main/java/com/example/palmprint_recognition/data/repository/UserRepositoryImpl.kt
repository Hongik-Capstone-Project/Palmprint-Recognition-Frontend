package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.api.UserApi
import com.example.palmprint_recognition.data.model.AddUserInstitutionRequest
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.model.ErrorResponse
import com.example.palmprint_recognition.data.model.PalmprintRegistrationStatusResponse
import com.example.palmprint_recognition.data.model.PaymentMethod
import com.example.palmprint_recognition.data.model.AddPaymentMethodRequest
import com.example.palmprint_recognition.data.model.RegisterPalmprintRequest
import com.example.palmprint_recognition.data.model.RegisterPalmprintResponse
import com.example.palmprint_recognition.data.model.UserInstitution
import com.example.palmprint_recognition.data.model.PagedResponse
import com.example.palmprint_recognition.data.model.UserVerificationLog
import com.example.palmprint_recognition.data.model.ReportVerificationRequest
import com.example.palmprint_recognition.data.model.ReportResponse

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

    override suspend fun getUserInstitutions(): List<UserInstitution> {
        return try {
            userApi.getUserInstitutions()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun addUserInstitution(
        institutionId: Int,
        institutionUserId: String
    ): UserInstitution {
        return try {
            val request = AddUserInstitutionRequest(
                institutionId = institutionId,
                institutionUserId = institutionUserId
            )
            userApi.addUserInstitution(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun deleteUserInstitution(institutionId: Int) {
        try {
            userApi.deleteUserInstitution(institutionId)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }




    override suspend fun getPaymentMethods(): List<PaymentMethod> {
        return try {
            userApi.getPaymentMethods()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun addPaymentMethod(cardName: String, cardId: String): PaymentMethod {
        return try {
            val request = AddPaymentMethodRequest(cardName = cardName, cardId = cardId)
            userApi.addPaymentMethod(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun deletePaymentMethod(paymentMethodId: Int) {
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

    override suspend fun registerPalmprint(palmprintData: String, userId: Int?): RegisterPalmprintResponse {
        return try {
            val request = RegisterPalmprintRequest(
                userId = userId,           // 필요 없으면 null로 두면 됨
                palmprintData = palmprintData
            )
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


    override suspend fun getUserVerifications(page: Int, size: Int): PagedResponse<UserVerificationLog> {
        return try {
            userApi.getUserVerifications(page = page, size = size)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun reportVerification(logId: String, reason: String): ReportResponse {
        return try {
            val request = ReportVerificationRequest(reason)
            userApi.reportVerification(logId, request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

}