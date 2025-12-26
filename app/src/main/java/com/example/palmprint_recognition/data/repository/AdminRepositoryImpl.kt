package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.api.AdminApi
import com.example.palmprint_recognition.data.model.AddUserRequest
import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserDetail
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.ErrorResponse
import com.example.palmprint_recognition.data.model.RegisterDeviceRequest
import com.example.palmprint_recognition.data.model.RegisterDeviceResponse
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.model.ReportListResponse
import com.example.palmprint_recognition.data.model.UserListResponse
import com.example.palmprint_recognition.data.model.VerificationListResponse
import com.example.palmprint_recognition.data.model.VerificationSummaryResponse
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val adminApi: AdminApi,
    private val gson: Gson
) : AdminRepository {

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

    override suspend fun getUserList(page: Int, size: Int): UserListResponse {
        return try {
            adminApi.getUserList(page, size)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getUserById(userId: Int): AdminUserDetail {
        return try {
            adminApi.getUserById(userId)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun addUser(name: String, email: String, password: String): AddUserResponse {
        return try {
            val request = AddUserRequest(name, email, password)
            adminApi.addUser(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun deleteUser(userId: Int) {
        try {
            adminApi.deleteUser(userId)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }


    override suspend fun getDeviceList(page: Int, size: Int): DeviceListResponse {
        return try {
            adminApi.getDeviceList(page, size)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getDeviceById(deviceId: Int): DeviceInfo {
        return try {
            adminApi.getDeviceById(deviceId)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun registerDevice(institutionId: Int, location: String): RegisterDeviceResponse {
        return try {
            val request = RegisterDeviceRequest(institutionId, location)
            adminApi.registerDevice(request)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun deleteDevice(deviceId: Int) {
        try {
            adminApi.deleteDevice(deviceId)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getVerificationList(
        page: Int,
        size: Int
    ): VerificationListResponse {
        return try {
            adminApi.getVerificationList(page, size)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getVerificationSummary(): VerificationSummaryResponse {
        return try {
            adminApi.getVerificationSummary()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getReportList(page: Int, size: Int): ReportListResponse {
        return try {
            adminApi.getReportList(page, size)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun getReportById(reportId: Int): ReportInfo {
        return try {
            adminApi.getReportById(reportId)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun updateReportStatus(reportId: Int, status: String): ReportInfo {
        return try {
            adminApi.updateReportStatus(reportId, status)
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }
}