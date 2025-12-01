package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.PalmprintListResponse
import com.example.palmprint_recognition.data.model.PalmprintUploadResponse
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.model.ReportListResponse
import com.example.palmprint_recognition.data.model.UpdateReportStatusResponse
import com.example.palmprint_recognition.data.model.UserListResponse
import com.example.palmprint_recognition.data.model.VerificationListResponse
import java.io.File

/**
 * 관리자 관련 기능에 대한 Repository 인터페이스 (설계도)
 */
interface AdminRepository {
    suspend fun getUserList(
        page: Int = 1,
        size: Int = 10
    ): UserListResponse

    suspend fun getUserById(userId: Int): AdminUserInfo

    suspend fun addUser(name: String, email: String, password: String): AddUserResponse

    suspend fun updateUser(userId: Int, name: String? = null, email: String? = null): AdminUserInfo

    suspend fun deleteUser(userId: Int)

    suspend fun getUserPalmprints(userId: Int): PalmprintListResponse

    suspend fun uploadPalmprint(imageFile: File): PalmprintUploadResponse

    suspend fun deletePalmprint(userId: Int, palmprintId: Int)

    suspend fun getDeviceList(
        page: Int = 1,
        size: Int = 10
    ): DeviceListResponse

    suspend fun getDeviceById(deviceId: Int): DeviceInfo

    suspend fun updateDevice(deviceId: Int, identifier: String? = null, memo: String? = null): DeviceInfo

    suspend fun registerDevice(identifier: String, memo: String): DeviceInfo

    suspend fun deleteDevice(deviceId: Int)

    suspend fun getVerificationList(
        page: Int = 1,
        size: Int = 10,
        userId: Int? = null,
        status: String? = null,
        sort: String? = null
    ): VerificationListResponse

    suspend fun getReportList(
        page: Int = 1,
        size: Int = 10
    ): ReportListResponse

    suspend fun getReportById(reportId: Int): ReportInfo

    suspend fun updateReportStatus(reportId: Int, status: String): UpdateReportStatusResponse
}