package com.example.palmprint_recognition.data.model

import com.google.gson.annotations.SerializedName

/**
 * 사용자에게 등록된 기관 정보 모델
 *
 * @property institutionId 기관 ID
 * @property institutionName 기관 이름
 * @property institutionUserId 해당 기관에서의 사용자 ID
 */
data class UserInstitutionInfo(
    @SerializedName("institution_id")
    val institutionId: String,
    @SerializedName("institution_name")
    val institutionName: String,
    @SerializedName("institution_user_id")
    val institutionUserId: String
)

/**
 * 사용자 기관 조회 API 응답의 data 필드 모델
 *
 * @property userId 사용자 ID
 * @property items 기관 정보 리스트
 * @property page 현재 페이지 번호
 * @property size 페이지당 항목 수
 * @property total 전체 항목 수
 * @property pages 전체 페이지 수
 * @property next 다음 페이지 번호 (없으면 null)
 * @property previous 이전 페이지 번호 (없으면 null)
 */
data class UserInstitutionsData(
    @SerializedName("user_id")
    val userId: String,
    val items: List<UserInstitutionInfo>,
    val page: Int,
    val size: Int,
    val total: Int,
    val pages: Int,
    val next: Int?,
    val previous: Int?
)

/**
 * 사용자 기관 조회 API의 전체 성공 응답 모델
 *
 * @property status 응답 상태
 * @property data 실제 데이터
 */
data class UserInstitutionsResponse(
    val status: String,
    val data: UserInstitutionsData
)

/**
 * 사용자 기관 추가 API의 요청 모델
 *
 * @property institutionName 기관 이름
 * @property institutionUserId 해당 기관에서의 사용자 ID
 */
data class AddUserInstitutionRequest(
    @SerializedName("institution_name")
    val institutionName: String,
    @SerializedName("institution_user_id")
    val institutionUserId: String
)

/**
 * 사용자 기관 추가 API 응답의 institution 필드 모델
 *
 * @property id 등록된 기관의 ID
 * @property name 등록된 기관의 이름
 * @property institutionUserId 해당 기관에서의 사용자 ID
 */
data class AddedInstitutionInfo(
    val id: String,
    val name: String,
    @SerializedName("institution_user_id")
    val institutionUserId: String
)

/**
 * 사용자 기관 추가 API의 전체 성공 응답 모델
 *
 * @property status 응답 상태
 * @property institution 등록된 기관 정보
 * @property userId 사용자 ID
 */
data class AddUserInstitutionResponse(
    val status: String,
    val institution: AddedInstitutionInfo,
    @SerializedName("user_id")
    val userId: String
)

/**
 * 결제 수단 정보 모델
 *
 * @property paymentMethodId 결제 수단 ID
 * @property cardName 카드 이름
 * @property cardId 카드 식별자 (마스킹된 번호 등)
 */
data class PaymentMethodInfo(
    @SerializedName("payment_method_id")
    val paymentMethodId: String,
    @SerializedName("card_name")
    val cardName: String,
    @SerializedName("card_id")
    val cardId: String
)

/**
 * 결제 수단 조회 API 응답의 data 필드 모델
 *
 * @property userId 사용자 ID
 * @property items 결제 수단 정보 리스트
 * @property page 현재 페이지 번호
 * @property size 페이지당 항목 수
 * @property total 전체 항목 수
 * @property pages 전체 페이지 수
 * @property next 다음 페이지 번호 (없으면 null)
 * @property previous 이전 페이지 번호 (없으면 null)
 */
data class PaymentMethodsData(
    @SerializedName("user_id")
    val userId: String,
    val items: List<PaymentMethodInfo>,
    val page: Int,
    val size: Int,
    val total: Int,
    val pages: Int,
    val next: Int?,
    val previous: Int?
)

/**
 * 결제 수단 조회 API의 전체 성공 응답 모델
 *
 * @property status 응답 상태
 * @property data 실제 데이터
 */
data class PaymentMethodsResponse(
    val status: String,
    val data: PaymentMethodsData
)

/**
 * 결제 수단 추가 API의 요청 모델
 *
 * @property cardName 카드 이름
 * @property cardId 카드 식별자
 */
data class AddPaymentMethodRequest(
    @SerializedName("card_name")
    val cardName: String,
    @SerializedName("card_id")
    val cardId: String
)

/**
 * 결제 수단 추가 API 응답의 payment_method 필드 모델
 *
 * @property cardName 카드 이름
 * @property cardId 카드 식별자
 */
data class AddedPaymentMethod(
    @SerializedName("card_name")
    val cardName: String,
    @SerializedName("card_id")
    val cardId: String
)

/**
 * 결제 수단 추가 API의 전체 성공 응답 모델
 *
 * @property status 응답 상태
 * @property userId 사용자 ID
 * @property paymentMethod 등록된 결제 수단 정보
 */
data class AddPaymentMethodResponse(
    val status: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("payment_method")
    val paymentMethod: AddedPaymentMethod
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

/**
 * 사용자 인증 내역 정보 모델
 *
 * @property logId 로그 ID
 * @property timestamp 인증 시각
 * @property institution 기관 이름
 * @property authType 인증 종류
 * @property result 인증 결과
 */
data class UserVerificationInfo(
    @SerializedName("log_id")
    val logId: String,
    val timestamp: String,
    val institution: String,
    @SerializedName("auth_type")
    val authType: String,
    val result: String
)

/**
 * 사용자 인증 내역 조회 API 응답의 data 필드 모델
 *
 * @property userId 사용자 ID
 * @property verifications 인증 내역 리스트
 * @property page 현재 페이지 번호
 * @property size 페이지당 항목 수
 * @property total 전체 항목 수
 * @property pages 전체 페이지 수
 * @property next 다음 페이지 번호 (없으면 null)
 * @property previous 이전 페이지 번호 (없으면 null)
 */
data class UserVerificationsData(
    @SerializedName("user_id")
    val userId: String,
    val verifications: List<UserVerificationInfo>,
    val page: Int,
    val size: Int,
    val total: Int,
    val pages: Int,
    val next: Int?,
    val previous: Int?
)

/**
 * 사용자 인증 내역 조회 API의 전체 성공 응답 모델
 *
 * @property status 응답 상태
 * @property data 실제 데이터
 */
data class UserVerificationsResponse(
    val status: String,
    val data: UserVerificationsData
)

/**
 * 인증 내역 신고 API의 요청 모델
 *
 * @property reason 신고 사유
 */
data class ReportVerificationRequest(
    val reason: String
)
