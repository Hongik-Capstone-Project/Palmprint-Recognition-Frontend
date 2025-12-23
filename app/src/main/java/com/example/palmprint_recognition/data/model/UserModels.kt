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
 * 손바닥 등록 여부 조회 API의 성공 응답 모델
 *
 * @property status 응답 상태
 * @property userId 사용자 ID
 * @property isPalmprintRegistered 손바닥 등록 여부
 */
data class PalmprintRegistrationStatusResponse(
    val status: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("palmprint_registered")
    val isPalmprintRegistered: Boolean
)

/**
 * 손바닥 등록 API의 요청 모델
 *
 * @property palmprintData Base64 인코딩된 손바닥 데이터
 */
data class RegisterPalmprintRequest(
    @SerializedName("palmprint_data")
    val palmprintData: String
)

/**
 * 손바닥 등록 API 응답의 palmprint 필드 모델
 *
 * @property palmprintId 등록된 손바닥의 ID
 * @property createdAt 등록 시각
 */
data class RegisteredPalmprintInfo(
    @SerializedName("palmprint_id")
    val palmprintId: String,
    @SerializedName("created_at")
    val createdAt: String
)

/**
 * 손바닥 등록 API의 전체 성공 응답 모델
 *
 * @property status 응답 상태
 * @property userId 사용자 ID
 * @property palmprint 등록된 손바닥 정보
 * @property message 성공 메시지
 */
data class RegisterPalmprintResponse(
    val status: String,
    @SerializedName("user_id")
    val userId: String,
    val palmprint: RegisteredPalmprintInfo,
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

