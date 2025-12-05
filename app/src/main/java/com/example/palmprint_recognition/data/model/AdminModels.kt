package com.example.palmprint_recognition.data.model

import com.google.gson.annotations.SerializedName

/**
 * 관리자 API에서 사용하는 사용자 정보 모델 (Get Users API 기준)
 *
 * @property id 사용자 ID
 * @property createdAt 생성 일시
 * @property email 사용자 이메일
 * @property name 사용자 이름
 * @property phoneNumber 전화번호
 */
data class AdminUserInfo(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val email: String,
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)

/**
 * 사용자 목록 조회 API의 성공 응답 모델
 *
 * @property items 사용자 정보 리스트
 * @property total 전체 항목 수
 * @property page 현재 페이지 번호
 * @property size 페이지당 항목 수
 * @property pages 전체 페이지 수
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
 *
 * @property name 사용자 이름
 * @property email 사용자 이메일
 * @property password 사용자 비밀번호
 */
data class AddUserRequest(
    val name: String,
    val email: String,
    val password: String
)

/**
 * 사용자 추가 API의 성공 응답 모델
 *
 * @property id 생성된 사용자의 ID
 */
data class AddUserResponse(
    val id: Int
)

/**
 * 관리자가 사용자 정보를 수정할 때 사용하는 요청 모델 (부분 수정)
 *
 * @property name 수정할 사용자 이름 (optional)
 * @property email 수정할 사용자 이메일 (optional)
 */
data class UpdateUserRequest(
    val name: String? = null,
    val email: String? = null
)

/**
 * 손바닥 정보 모델
 *
 * @property id 손바닥 정보 ID
 * @property userId 사용자 ID
 * @property createdAt 등록 일시
 */
data class PalmprintInfo(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("created_at")
    val createdAt: String
)

/**
 * 특정 사용자의 손바닥 정보 목록 응답 모델
 *
 * @property data 손바닥 정보 리스트
 */
data class PalmprintListResponse(
    val data: List<PalmprintInfo>
)

/**
 * 손바닥 정보 등록 API의 성공 응답 모델
 *
 * @property uploaded 업로드된 파일 수
 * @property item 등록된 손바닥 정보
 */
data class PalmprintUploadResponse(
    val uploaded: Int,
    val item: PalmprintInfo
)

/**
 * 디바이스 정보 모델
 *
 * @property id 디바이스 ID
 * @property createdAt 등록 일시
 * @property institutionId 기관 ID
 * @property firmwareVersion 펌웨어 버전
 * @property location 위치
 * @property status 상태
 */
data class DeviceInfo(
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("institution_id")
    val institutionId: Int,
    @SerializedName("firmware_version")
    val firmwareVersion: String,
    val location: String,
    val status: String
)

/**
 * 디바이스 목록 조회 API의 성공 응답 모델
 *
 * @property items 디바이스 정보 리스트
 * @property total 전체 항목 수
 * @property page 현재 페이지 번호
 * @property size 페이지당 항목 수
 * @property pages 전체 페이지 수
 */
data class DeviceListResponse(
    val items: List<DeviceInfo>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int
)

/**
 * 관리자가 디바이스 정보를 수정할 때 사용하는 요청 모델 (부분 수정)
 *
 * @property identifier 수정할 디바이스 식별자 (optional)
 * @property memo 수정할 메모 (optional)
 */
data class UpdateDeviceRequest(
    val identifier: String? = null,
    val memo: String? = null
)

/**
 * 관리자가 새로운 디바이스를 등록할 때 사용하는 요청 모델
 *
 * @property identifier 디바이스 식별자
 * @property memo 메모
 */
data class RegisterDeviceRequest(
    val identifier: String,
    val memo: String
)

/**
 * 인증 내역 정보 모델
 *
 * @property id 인증 내역 ID
 * @property user 인증한 사용자 정보
 * @property status 인증 상태 ("approved", "rejected")
 * @property createdAt 인증 시각
 */
data class VerificationRecord(
    @SerializedName("_id")
    val id: String,
    val user: AdminUserInfo,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String
)

/**
 * 인증 내역 목록 조회 API의 성공 응답 모델
 *
 * @property items 인증 내역 리스트
 * @property total 전체 항목 수
 * @property page 현재 페이지 번호
 * @property size 페이지당 항목 수
 * @property pages 전체 페이지 수
 * @property next 다음 페이지 번호 (없으면 null)
 * @property previous 이전 페이지 번호 (없으면 null)
 */
data class VerificationListResponse(
    val items: List<VerificationRecord>,
    val total: Int,
    val page: Int,
    val size: Int,
    val pages: Int,
    val next: Int?,
    val previous: Int?
)

/**
 * 신고 내역 정보 모델
 *
 * @property id 신고 ID
 * @property createdAt 생성 시각
 * @property userId 사용자 ID
 * @property reportType 신고 타입
 * @property description 신고 내용
 * @property status 처리 상태
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
 *
 * @property items 신고 내역 리스트
 * @property total 전체 항목 수
 * @property page 현재 페이지 번호
 * @property size 페이지당 항목 수
 * @property pages 전체 페이지 수
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
 *
 * @property status 변경할 상태 ("approved" 또는 "rejected")
 */
data class UpdateReportStatusRequest(
    val status: String
)

/**
 * 수정된 신고 내역 정보 모델
 *
 * @property id 신고 ID
 * @property verificationLogId 관련 인증 로그 ID
 * @property reason 신고 사유
 * @property status 신고 처리 상태
 * @property user 신고한 사용자 정보
 * @property updatedAt 수정 시각
 */
data class UpdatedReportInfo(
    val id: Int,
    @SerializedName("_id")
    val verificationLogId: String,
    val reason: String,
    val status: String,
    val user: AdminUserInfo,
    @SerializedName("updated_at")
    val updatedAt: String
)

/**
 * 신고 내역 수정 API의 성공 응답 모델
 *
 * @property status 변경된 상태
 * @property report 수정된 신고 내역 정보
 * @property message 성공 메시지
 */
data class UpdateReportStatusResponse(
    val status: String,
    val report: UpdatedReportInfo,
    val message: String
)
