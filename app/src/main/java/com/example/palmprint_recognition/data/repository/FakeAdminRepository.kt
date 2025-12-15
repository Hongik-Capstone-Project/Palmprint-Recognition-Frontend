package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserDetail
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.LoginResponse
import com.example.palmprint_recognition.data.model.LogoutResponse
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.model.ReportListResponse
import com.example.palmprint_recognition.data.model.SignUpResponse
import com.example.palmprint_recognition.data.model.UserInstitutionSimple
import com.example.palmprint_recognition.data.model.UserListResponse
import com.example.palmprint_recognition.data.model.VerificationListResponse
import com.example.palmprint_recognition.data.model.VerificationSummaryResponse
import java.io.File
import javax.inject.Inject

// AdminRepository와 AuthRepository를 모두 구현하도록 변경
class FakeAdminRepository @Inject constructor() : AdminRepository, AuthRepository {

    private var loggedIn = false

    // --- AdminRepository 구현 ---
    override suspend fun getUserList(page: Int, size: Int): UserListResponse {
        val fakeUsers = listOf(
            AdminUserInfo(1, "2025-12-01T10:00:00Z", "chulsoo.kim@example.com", "김철수 (가짜)", null),
            AdminUserInfo(2, "2025-12-01T11:00:00Z", "younghee.lee@example.com", "이영희 (가짜)", null),
            AdminUserInfo(3, "2025-12-01T12:00:00Z", "minjun.park@example.com", "박민준 (가짜)", null)
        )
        return UserListResponse(fakeUsers, fakeUsers.size, 1, 10, 1)
    }

    override suspend fun getUserById(userId: Int): AdminUserDetail {
        // 가짜 사용자 상세 정보 생성
        return AdminUserDetail(
            id = userId,
            name = "가짜 사용자 상세",
            email = "fake.detail@example.com",
            isPalmRegistered = true,
            userInstitutions = listOf(
                UserInstitutionSimple("inst_112", "Hongik University"),
                UserInstitutionSimple("inst_113", "LG")
            )
        )
    }

    override suspend fun addUser(name: String, email: String, password: String, isAdmin: Boolean): AddUserResponse {
        return AddUserResponse(999, "Fake user created successfully.")
    }

    override suspend fun deleteUser(userId: Int) {}

    override suspend fun getDeviceList(page: Int, size: Int): DeviceListResponse {
        val fakeDevices = listOf(
            DeviceInfo(1, "Hongik University", "Lobby", "2025-12-01T10:00:00Z"),
            DeviceInfo(2, "LG CNS", "Office", "2025-12-01T11:00:00Z")
        )
        return DeviceListResponse(fakeDevices, fakeDevices.size, 1, 10, 1)
    }

    override suspend fun getDeviceById(deviceId: Int): DeviceInfo {
        return DeviceInfo(deviceId, "Fake Institution", "Fake Location", "")
    }

    override suspend fun registerDevice(id: Int, institutionName: String, location: String): DeviceInfo {
        return DeviceInfo(id, institutionName, location, "")
    }

    override suspend fun deleteDevice(deviceId: Int) {}

    override suspend fun getVerificationList(
        page: Int,
        size: Int
    ): VerificationListResponse {
        return VerificationListResponse(emptyList(), 0, 1, 10, 1)
    }

    override suspend fun getVerificationSummary(): VerificationSummaryResponse {
        return VerificationSummaryResponse(
            totalUsers = 150,
            registeredPalms = 120,
            totalVerifications = 3500,
            successRate = 98.5
        )
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

    override suspend fun updateReportStatus(reportId: Int, status: String): ReportInfo {
        return ReportInfo(reportId, "", 1, "fake_type", "Fake description", status)
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