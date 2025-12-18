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

// --- 사용자 상세 정보 모델 (GET /api/admin/users/{user_id}) ---

data class AdminPaymentMethod(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("card_name")
    val cardName: String,
    @SerializedName("card_id")
    val cardId: String
)

data class AdminReport(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("auth_log_id")
    val authLogId: Int,
    @SerializedName("report_type")
    val reportType: String,
    val description: String,
    val status: String
)

data class AdminInstitution(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val name: String,
    val address: String?
)

data class AdminUserInstitution(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user_id")
    val userId: Int,
    val institution: AdminInstitution,
    @SerializedName("institution_user_id")
    val institutionUserId: String
)

data class AdminRole(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val name: String
)

data class AdminUserInstitutionRole(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user_id")
    val userId: Int,
    val role: AdminRole,
    @SerializedName("institution_id")
    val institutionId: Int
)

/**
 * 관리자 API의 사용자 상세 정보 모델
 */
data class AdminUserDetail(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("created_at")
    val createdAt: String,
    val isPalmRegistered: Boolean,
    @SerializedName("payment_methods")
    val paymentMethods: List<AdminPaymentMethod>,
    val reports: List<AdminReport>,
    @SerializedName("user_institutions")
    val userInstitutions: List<AdminUserInstitution>,
    @SerializedName("user_institution_roles")
    val userInstitutionRoles: List<AdminUserInstitutionRole>
)

// --- End of 사용자 상세 정보 모델 ---


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
    val password: String
) {
    @SerializedName("is_admin")
    val isAdmin: Boolean = false
}

/**
 * 사용자 추가 API의 성공 응답 모델
 */
data class AddUserResponse(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val email: String,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String?
)



/**
 * 디바이스 정보 모델 (목록/상세 조회 시 사용)
 */
data class DeviceInfo(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val institution: AdminInstitution,
    val location: String
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
    @SerializedName("institution_id")
    val institutionId: Int,
    val location: String
)

/**
 * 디바이스 추가 API의 성공 응답 모델
 */
data class RegisterDeviceResponse(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("institution_id")
    val institutionId: Int,
    val location: String
)


/**
 * 인증 내역 정보 모델 (GET /api/admin/verifications)
 */
data class VerificationRecord(
    val id: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("institution_id")
    val institutionId: Int,
    @SerializedName("institution_name")
    val institutionName: String,
    val location: String,
    @SerializedName("is_success")
    val isSuccess: Boolean,
    @SerializedName("auth_type")
    val authType: String
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
