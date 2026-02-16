package com.example.palmprint_recognition.data.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize



data class PagedResponse<T>(
    val items: List<T>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)

data class Institution(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val name: String,
    val address: String? // nullable
)

/**
 * /api/users/me/institutions 의 아이템(= 사용자-기관 연결)
 */
data class UserInstitution(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val institution: Institution,
    @SerializedName("institution_user_id")
    val institutionUserId: String
)

/**
 * 기관 추가 요청
 * POST /api/users/me/institutions
 */
data class AddUserInstitutionRequest(
    @SerializedName("institution_id")
    val institutionId: Int,
    @SerializedName("institution_user_id")
    val institutionUserId: String
)




data class PaymentMethod(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("card_name")
    val cardName: String,
    @SerializedName("card_id")
    val cardId: String
)

data class AddPaymentMethodRequest(
    @SerializedName("card_name")
    val cardName: String,
    @SerializedName("card_id")
    val cardId: String
)

/**
 * GET /api/users/me/palmprints 응답
 */
data class GetMyPalmsResponse(
    val palms: List<PalmInfo> = emptyList(),

    @SerializedName("total_count")
    val totalCount: Int = 0
)

/**
 * palms 배열 안의 원소
 */
data class PalmInfo(
    val id: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("updated_at")
    val updatedAt: String
)

/**
 * POST /api/users/me/palmprints 요청 바디
 */
data class RegisterPalmprintRequest(
    @SerializedName("palmprint_data")
    val palmprintData: String
)

/**
 * POST /api/users/me/palmprints 응답
 * 예시:
 * {
 *   "id": 0,
 *   "created_at": "...",
 *   "user_id": 0,
 *   "message": "Palm registered successfully"
 * }
 */
data class RegisterPalmprintResponse(
    val id: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("user_id")
    val userId: Int,

    val message: String
)


@Parcelize
data class UserVerificationLog(
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
) : Parcelable

data class ReportVerificationRequest(
    val reason: String
)

data class ReportResponse(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("auth_log_id")
    val authLogId: String,
    @SerializedName("report_type")
    val reportType: String,
    val description: String,
    val status: String
)

