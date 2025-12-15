package com.example.palmprint_recognition.data.api

import com.example.palmprint_recognition.data.model.AddUserRequest
import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserDetail
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.RegisterDeviceRequest
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.model.ReportListResponse
import com.example.palmprint_recognition.data.model.UpdateReportStatusRequest
import com.example.palmprint_recognition.data.model.UserListResponse
import com.example.palmprint_recognition.data.model.VerificationListResponse
import com.example.palmprint_recognition.data.model.VerificationSummaryResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 관리자 관련 REST API 인터페이스
 */
interface AdminApi {

    /**
     * 전체 사용자 목록을 페이지네이션하여 조회하는 API
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @return 사용자 목록과 페이지 정보
     */
    @GET("/api/admin/users")
    suspend fun getUserList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): UserListResponse

    /**
     * 특정 사용자 ID로 사용자 정보를 조회하는 API
     *
     * @param userId 조회할 사용자의 ID
     * @return 특정 사용자의 상세 정보
     */
    @GET("/api/admin/users/{user_id}")
    suspend fun getUserById(
        @Path("user_id") userId: Int
    ): AdminUserDetail

    /**
     * 관리자가 새로운 사용자를 추가하는 API
     *
     * @param request 추가할 사용자의 정보 (이름, 이메일, 비밀번호)
     * @return 생성된 사용자의 ID
     */
    @POST("/api/admin/users")
    suspend fun addUser(
        @Body request: AddUserRequest
    ): AddUserResponse

    /**
     * 관리자가 특정 사용자를 삭제하는 API
     *
     * @param userId 삭제할 사용자의 ID
     */
    @DELETE("/api/admin/users/{user_id}")
    suspend fun deleteUser(
        @Path("user_id") userId: Int
    ): Unit


    /**
     * 전체 디바이스 목록을 페이지네이션하여 조회하는 API
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @return 디바이스 목록과 페이지 정보
     */
    @GET("/api/admin/devices")
    suspend fun getDeviceList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): DeviceListResponse

    /**
     * 특정 디바이스 ID로 디바이스 정보를 조회하는 API
     *
     * @param deviceId 조회할 디바이스의 ID
     * @return 특정 디바이스 정보
     */
    @GET("/api/admin/devices/{device_id}")
    suspend fun getDeviceById(
        @Path("device_id") deviceId: Int
    ): DeviceInfo

    /**
     * 새로운 디바이스를 등록하는 API
     *
     * @param request 등록할 디바이스 정보
     * @return 등록된 디바이스의 전체 정보
     */
    @POST("/api/admin/devices")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequest
    ): DeviceInfo

    /**
     * 특정 디바이스를 삭제하는 API
     *
     * @param deviceId 삭제할 디바이스의 ID
     */
    @DELETE("/api/admin/devices/{device_id}")
    suspend fun deleteDevice(
        @Path("device_id") deviceId: Int
    ): Unit

    /**
     * 전체 인증 내역을 페이지네이션하여 조회하는 API
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @return 인증 내역 목록과 페이지 정보
     */
    @GET("/api/admin/verifications")
    suspend fun getVerificationList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): VerificationListResponse

    /**
     * 인증 통계 요약을 조회하는 API
     *
     * @return 인증 통계 요약 정보
     */
    @GET("/api/admin/verifications/summary")
    suspend fun getVerificationSummary(): VerificationSummaryResponse

    /**
     * 전체 신고 내역을 페이지네이션하여 조회하는 API
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @return 신고 내역 목록과 페이지 정보
     */
    @GET("/api/admin/reports")
    suspend fun getReportList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): ReportListResponse

    /**
     * 특정 신고 내역을 ID로 조회하는 API
     *
     * @param reportId 조회할 신고 내역의 ID
     * @return 특정 신고 내역 정보
     */
    @GET("/api/admin/reports/{report_id}")
    suspend fun getReportById(
        @Path("report_id") reportId: Int
    ): ReportInfo

    /**
     * 특정 신고 내역의 상태를 수정하는 API
     *
     * @param reportId 수정할 신고 내역의 ID
     * @param request 변경할 상태 정보
     * @return 수정된 신고 내역 정보와 성공 메시지
     */
    @PATCH("/api/admin/reports/{report_id}")
    suspend fun updateReportStatus(
        @Path("report_id") reportId: Int,
        @Body request: UpdateReportStatusRequest
    ): ReportInfo

}
