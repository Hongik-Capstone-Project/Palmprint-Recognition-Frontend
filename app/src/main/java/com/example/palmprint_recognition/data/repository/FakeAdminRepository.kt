package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.LoginResponse
import com.example.palmprint_recognition.data.model.LogoutResponse
import com.example.palmprint_recognition.data.model.PalmprintListResponse
import com.example.palmprint_recognition.data.model.PalmprintUploadResponse
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.model.ReportListResponse
import com.example.palmprint_recognition.data.model.SignUpResponse
import com.example.palmprint_recognition.data.model.UpdateReportStatusResponse
import com.example.palmprint_recognition.data.model.UserListResponse
import com.example.palmprint_recognition.data.model.VerificationListResponse
import java.io.File
import javax.inject.Inject

// AdminRepository와 AuthRepository를 모두 구현하도록 변경
class FakeAdminRepository @Inject constructor() : AdminRepository, AuthRepository {

    private var loggedIn = false

    // --- AdminRepository 구현 ---
    override suspend fun getUserList(page: Int, size: Int): UserListResponse {
        val fakeUsers = listOf(
            AdminUserInfo(1, "2025-12-01T10:00:00Z", "chulsoo.kim@example.com", "김철수 (가짜)", "010-1234-5678"),
            AdminUserInfo(2, "2025-12-01T11:00:00Z", "younghee.lee@example.com", "이영희 (가짜)", "010-2345-6789"),
            AdminUserInfo(3, "2025-12-01T12:00:00Z", "minjun.park@example.com", "박민준 (가짜)", "010-3456-7890")
        )
        return UserListResponse(fakeUsers, fakeUsers.size, 1, 10, 1)
    }

    override suspend fun getUserById(userId: Int): AdminUserInfo {
        return AdminUserInfo(userId, "", "fake.user@example.com", "가짜 사용자", "")
    }

    override suspend fun addUser(name: String, email: String, password: String): AddUserResponse {
        return AddUserResponse(999)
    }

    override suspend fun updateUser(userId: Int, name: String?, email: String?): AdminUserInfo {
        return AdminUserInfo(userId, "", email ?: "sujung@example.com", name ?: "수정된 이름", "")
    }

    override suspend fun deleteUser(userId: Int) {}

    override suspend fun getUserPalmprints(userId: Int): PalmprintListResponse {
        return PalmprintListResponse(emptyList())
    }

    override suspend fun uploadPalmprint(imageFile: File): PalmprintUploadResponse {
        throw NotImplementedError("Fake uploadPalmprint is not implemented")
    }

    override suspend fun deletePalmprint(userId: Int, palmprintId: Int) {}

    override suspend fun getDeviceList(page: Int, size: Int): DeviceListResponse {
        val fakeDevices = listOf(
            DeviceInfo(1, "2025-12-01T10:00:00Z", 1, "1.0.0", "Lobby", "active"),
            DeviceInfo(2, "2025-12-01T11:00:00Z", 1, "1.0.2", "Office", "inactive")
        )
        return DeviceListResponse(fakeDevices, fakeDevices.size, 1, 10, 1)
    }

    override suspend fun getDeviceById(deviceId: Int): DeviceInfo {
        return DeviceInfo(deviceId, "", 1, "1.0.0", "Fake Location", "active")
    }

    override suspend fun updateDevice(deviceId: Int, identifier: String?, memo: String?): DeviceInfo {
        // Note: This fake implementation does not use identifier and memo.
        return DeviceInfo(deviceId, "", 1, "1.0.1", "Updated Location", "active")
    }

    override suspend fun registerDevice(identifier: String, memo: String): DeviceInfo {
        // Note: This fake implementation does not use identifier and memo.
        return DeviceInfo(999, "", 1, "1.0.0", "New Device", "active")
    }

    override suspend fun deleteDevice(deviceId: Int) {}

    override suspend fun getVerificationList(
        page: Int,
        size: Int,
        userId: Int?,
        status: String?,
        sort: String?
    ): VerificationListResponse {
        return VerificationListResponse(emptyList(), 0, 1, 10, 1, null, null)
    }

    override suspend fun getReportList(page: Int, size: Int): ReportListResponse {
        val fakeReports = listOf(
            ReportInfo(1, "2025-12-01T10:00:00Z", 1, "suspicious_activity", "Suspicious activity detected at the main gate.", "pending"),
            ReportInfo(2, "2025-12-01T11:00:00Z", 2, "unauthorized_access", "Unauthorized access attempt at the lab.", "resolved")
        )
        return ReportListResponse(fakeReports, fakeReports.size, 1, 10, 1)
    }

    override suspend fun getReportById(reportId: Int): ReportInfo {
        return ReportInfo(reportId, "", 1, "fake_type", "Fake description", "pending")
    }

    override suspend fun updateReportStatus(reportId: Int, status: String): UpdateReportStatusResponse {
        throw NotImplementedError("Fake updateReportStatus is not implemented")
    }

    // --- AuthRepository 구현 ---

    override suspend fun signup(name: String, email: String, password: String): SignUpResponse {
        throw NotImplementedError("Fake signup is not implemented")
    }

    override suspend fun signout() {
        loggedIn = false
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        loggedIn = true
        return LoginResponse(
            status = "success",
            userId = "fake_user_id",
            accessToken = "fake_access_token",
            refreshToken = "fake_refresh_token"
        )
    }

    override suspend fun logout(): LogoutResponse {
        loggedIn = false
        return LogoutResponse("success", "Logged out successfully")
    }

    override fun getSavedRole(): String? {
        return if (loggedIn) "admin" else null
    }

    override fun saveRole(role: String) {}

    override fun isLoggedIn(): Boolean {
        return loggedIn
    }
}