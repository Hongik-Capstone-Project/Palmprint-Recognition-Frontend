package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserDetail
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.model.ReportListResponse
import com.example.palmprint_recognition.data.model.UserListResponse
import com.example.palmprint_recognition.data.model.VerificationListResponse
import com.example.palmprint_recognition.data.model.VerificationSummaryResponse
import java.io.File

/**
 * 관리자 관련 기능에 대한 Repository 인터페이스 (설계도)
 */
interface AdminRepository {
    suspend fun getUserList(
        page: Int = 1,
        size: Int = 10
    ): UserListResponse

    suspend fun getUserById(userId: Int): AdminUserDetail

    suspend fun addUser(name: String, email: String, password: String, isAdmin: Boolean): AddUserResponse

    suspend fun deleteUser(userId: Int)

    suspend fun getDeviceList(
        page: Int = 1,
        size: Int = 10
    ): DeviceListResponse

    suspend fun getDeviceById(deviceId: Int): DeviceInfo

    suspend fun registerDevice(id: Int, institutionName: String, location: String): DeviceInfo

    suspend fun deleteDevice(deviceId: Int)

    suspend fun getVerificationList(
        page: Int = 1,
        size: Int = 10
    ): VerificationListResponse

    suspend fun getVerificationSummary(): VerificationSummaryResponse

    suspend fun getReportList(
        page: Int = 1,
        size: Int = 10
    ): ReportListResponse

    suspend fun getReportById(reportId: Int): ReportInfo

    suspend fun updateReportStatus(reportId: Int, status: String): ReportInfo
}