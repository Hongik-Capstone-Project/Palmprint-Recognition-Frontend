package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.api.DemoApi
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.model.DemoRegisterRequest
import com.example.palmprint_recognition.data.model.DemoRegisterResponse
import com.example.palmprint_recognition.data.model.DemoVerifyRequest
import com.example.palmprint_recognition.data.model.DemoVerifyResponse
import com.example.palmprint_recognition.data.model.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class DemoRepositoryImpl @Inject constructor(
    private val demoApi: DemoApi,
    private val gson: Gson
) : DemoRepository {

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

    override suspend fun registerDemoPalmprint(
        name: String,
        palmprintData: String
    ): DemoRegisterResponse {
        return try {
            val request = DemoRegisterRequest(
                name = name,
                palmprintData = palmprintData
            )
            demoApi.registerDemoPalmprint(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun verifyDemoPalmprint(
        palmprintData: String
    ): DemoVerifyResponse {
        return try {
            val request = DemoVerifyRequest(
                palmprintData = palmprintData
            )
            demoApi.verifyDemoPalmprint(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }
}