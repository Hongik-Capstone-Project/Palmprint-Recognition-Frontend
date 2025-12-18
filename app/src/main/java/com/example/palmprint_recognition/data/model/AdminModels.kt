package com.example.palmprint_recognition.data.model

import com.google.gson.annotations.SerializedName

/**
 * 관리자 API의 사용자 목록에 사용되는 간소화된 사용자 정보 모델
 */
data class AdminUserInfo(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val email: String,
    val name: String,
)

/**
 * 사용자 상세 정보에 포함되는 기관 정보
 */
data class UserInstitutionSimple(
    @SerializedName("institution_id")
    val institutionId: String,
    @SerializedName("institution_name")
    val institutionName: String
)

/**
 * 관리자 API의 사용자 상세 정보 모델
 */
data class AdminUserDetail(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName(value = "isPalmRegistered", alternate = ["is_palm_registered"])
    val isPalmRegistered: Boolean,
    @SerializedName("user_institutions")
    val userInstitutions: List<UserInstitutionSimple>
)

/**
 * 사용자 목록 조회 API의 성공 응답 모델
 */
data class UserListResponse(
    val items: List<AdminUserInfo>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)


/**
 * 관리자가 사용자를 추가할 때 사용하는 요청 모델
 */
data class AddUserRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean
)

/**
 * 사용자 추가 API의 성공 응답 모델
 */
data class AddUserResponse(
    val id: Int,
    val name: String?,
    val message: String
)



/**
 * 디바이스 정보 모델
 */
data class DeviceInfo(
    val id: Int,
    @SerializedName("institution_name")
    val institutionName: String,
    val location: String,
    @SerializedName("created_at")
    val createdAt: String
)

/**
 * 디바이스 목록 조회 API의 성공 응답 모델
 */
data class DeviceListResponse(
    val items: List<DeviceInfo>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)

/**
 * 관리자가 새로운 디바이스를 등록할 때 사용하는 요청 모델
 */
data class RegisterDeviceRequest(
    val id: Int,
    @SerializedName("institution_name")
    val institutionName: String,
    val location: String
)

/**
 * 인증 내역에 포함된 사용자 정보 모델
 */
data class VerificationUser(
    val id: Int,
    val name: String,
    val email: String
)

/**
 * 인증 내역에 포함된 디바이스 정보 모델
 */
data class VerificationDevice(
    val id: Int,
    @SerializedName("institution_name")
    val institutionName: String,
    val location: String
)

/**
 * 인증 내역 정보 모델
 */
data class VerificationRecord(
    @SerializedName("_id")
    val id: String,
    val user: VerificationUser,
    val device: VerificationDevice,
    val success: Boolean,
    @SerializedName("created_at")
    val createdAt: String
)

/**
 * 인증 내역 목록 조회 API의 성공 응답 모델
 */
data class VerificationListResponse(
    val items: List<VerificationRecord>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)

/**
 * 인증 내역 요약 API의 성공 응답 모델
 */
data class VerificationSummaryResponse(
    @SerializedName("total_users")
    val totalUsers: Int,
    @SerializedName("registered_palms")
    val registeredPalms: Int,
    @SerializedName("total_verifications")
    val totalVerifications: Int,
    @SerializedName("success_rate")
    val successRate: Double
)

/**
 * 신고 내역 정보 모델
 */
data class ReportInfo(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("report_type")
    val reportType: String,
    val description: String,
    val status: String
)

/**
 * 신고 내역 목록 조회 API의 성공 응답 모델
 */
data class ReportListResponse(
    val items: List<ReportInfo>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)

/**
 * 신고 내역 수정 API의 요청 모델
 */
data class UpdateReportStatusRequest(
    val status: String
)
