package com.example.palmprint_recognition.data.api

import com.example.palmprint_recognition.data.model.AddUserRequest
import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserDetail
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.RegisterDeviceRequest
import com.example.palmprint_recognition.data.model.RegisterDeviceResponse
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.model.ReportListResponse
import com.example.palmprint_recognition.data.model.UserListResponse
import com.example.palmprint_recognition.data.model.VerificationListResponse
import com.example.palmprint_recognition.data.model.VerificationSummaryResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 관리자 관련 REST API 인터페이스
 */
interface AdminApi {

    /**
     * 전체 사용자 목록을 페이지네이션하여 조회하는 API
     */
    @GET("/api/admin/users")
    suspend fun getUserList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): UserListResponse

    /**
     * 특정 사용자 ID로 사용자 정보를 조회하는 API
     */
    @GET("/api/admin/users/{user_id}")
    suspend fun getUserById(
        @Path("user_id") userId: Int
    ): AdminUserDetail

    /**
     * 관리자가 새로운 사용자를 추가하는 API
     */
    @POST("/api/admin/users")
    suspend fun addUser(
        @Body request: AddUserRequest
    ): AddUserResponse

    /**
     * 관리자가 특정 사용자를 삭제하는 API
     */
    @DELETE("/api/admin/users/{user_id}")
    suspend fun deleteUser(
        @Path("user_id") userId: Int
    ): Unit


    /**
     * 전체 디바이스 목록을 페이지네이션하여 조회하는 API
     */
    @GET("/api/admin/devices")
    suspend fun getDeviceList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): DeviceListResponse

    /**
     * 특정 디바이스 ID로 디바이스 정보를 조회하는 API
     */
    @GET("/api/admin/devices/{device_id}")
    suspend fun getDeviceById(
        @Path("device_id") deviceId: Int
    ): DeviceInfo

    /**
     * 새로운 디바이스를 등록하는 API
     */
    @POST("/api/admin/devices")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequest
    ): RegisterDeviceResponse

    /**
     * 특정 디바이스를 삭제하는 API
     */
    @DELETE("/api/admin/devices/{device_id}")
    suspend fun deleteDevice(
        @Path("device_id") deviceId: Int
    ): Unit

    /**
     * 전체 인증 내역을 페이지네이션하여 조회하는 API
     */
    @GET("/api/admin/verifications")
    suspend fun getVerificationList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): VerificationListResponse

    /**
     * 인증 통계 요약을 조회하는 API
     */
    @GET("/api/admin/verifications/summary")
    suspend fun getVerificationSummary(): VerificationSummaryResponse

    /**
     * 전체 신고 내역을 페이지네이션하여 조회하는 API
     */
    @GET("/api/admin/reports")
    suspend fun getReportList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): ReportListResponse

    /**
     * 특정 신고 내역을 ID로 조회하는 API
     */
    @GET("/api/admin/reports/{report_id}")
    suspend fun getReportById(
        @Path("report_id") reportId: Int
    ): ReportInfo

    /**
     * 특정 신고 내역의 상태를 수정하는 API
     */
    @PATCH("/api/admin/reports/{report_id}/status")
    suspend fun updateReportStatus(
        @Path("report_id") reportId: Int,
        @Query("new_status") newStatus: String
    ): ReportInfo

}