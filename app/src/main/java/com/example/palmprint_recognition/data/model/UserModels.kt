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
 * 손바닥 등록 여부 조회 응답 (백엔드 미완성 대비: 필드 nullable)
 */
data class PalmprintRegistrationStatusResponse(
    // 서버가 status/message를 줄 수도, 안 줄 수도 있어서 nullable
    val status: String? = null,

    @SerializedName("user_id")
    val userId: Int? = null,

    // 서버가 palmprint_registered 또는 is_registered 등으로 바뀔 수 있어 현재는 하나로 유지
    @SerializedName("palmprint_registered")
    val isPalmprintRegistered: Boolean = false,

    // 추후 서버에서 palmprint_id 같은 걸 내려주면 받을 수 있게 확장 포인트
    @SerializedName("palmprint_id")
    val palmprintId: String? = null
)

/**
 * 손바닥 등록 요청
 * - 지금은 base64로 보낸다고 가정
 * - 추후 서버가 user_id를 요구하면 userId 사용
 */
data class RegisterPalmprintRequest(
    @SerializedName("user_id")
    val userId: Int? = null,

    @SerializedName("palmprint_data")
    val palmprintData: String
)

/**
 * 손바닥 등록 응답 (백엔드 미완성 대비: 모두 nullable)
 */
data class RegisterPalmprintResponse(
    val status: String? = null,

    @SerializedName("user_id")
    val userId: Int? = null,

    // 서버가 palmprint 객체를 줄 수도 / 안 줄 수도 있음
    val palmprint: RegisteredPalmprintInfo? = null,

    val message: String? = null
)

data class RegisteredPalmprintInfo(
    @SerializedName("palmprint_id")
    val palmprintId: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
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

