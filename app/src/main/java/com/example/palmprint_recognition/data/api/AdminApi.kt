package com.example.palmprint_recognition.data.api

import com.example.palmprint_recognition.data.model.AddUserRequest
import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.PalmprintListResponse
import com.example.palmprint_recognition.data.model.PalmprintUploadResponse
import com.example.palmprint_recognition.data.model.RegisterDeviceRequest
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.model.ReportListResponse
import com.example.palmprint_recognition.data.model.UpdateDeviceRequest
import com.example.palmprint_recognition.data.model.UpdateReportStatusRequest
import com.example.palmprint_recognition.data.model.UpdateReportStatusResponse
import com.example.palmprint_recognition.data.model.UpdateUserRequest
import com.example.palmprint_recognition.data.model.UserListResponse
import com.example.palmprint_recognition.data.model.VerificationListResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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
     * @param search 이름 또는 이메일 검색어 (optional)
     * @param sort 정렬 기준 (optional)
     * @return 사용자 목록과 페이지 정보
     */
    @GET("/api/admin/users")
    suspend fun getUserList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = null
    ): UserListResponse

    /**
     * 특정 사용자 ID로 사용자 정보를 조회하는 API
     *
     * @param userId 조회할 사용자의 ID
     * @return 특정 사용자 정보
     */
    @GET("/api/admin/users/{user_id}")
    suspend fun getUserById(
        @Path("user_id") userId: Int
    ): AdminUserInfo

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
     * 관리자가 특정 사용자의 정보를 수정하는 API (부분 수정)
     *
     * @param userId 수정할 사용자의 ID
     * @param request 수정할 사용자의 정보 (이름, 이메일)
     * @return 수정된 사용자의 전체 정보
     */
    @PATCH("/api/admin/users/{user_id}")
    suspend fun updateUser(
        @Path("user_id") userId: Int,
        @Body request: UpdateUserRequest
    ): AdminUserInfo

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
     * 특정 사용자의 손바닥 정보 목록을 조회하는 API
     *
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 손바닥 정보 리스트
     */
    @GET("/api/admin/users/{user_id}/palmprints")
    suspend fun getUserPalmprints(
        @Path("user_id") userId: Int
    ): PalmprintListResponse

    /**
     * 손바닥 정보 파일을 업로드하는 API
     *
     * @param palmprintImage 업로드할 손바닥 이미지 파일
     * @return 업로드 결과 정보
     */
    @Multipart
    @POST("/api/admin/users/palmprints")
    suspend fun uploadPalmprint(
        @Part palmprintImage: MultipartBody.Part
    ): PalmprintUploadResponse

    /**
     * 특정 사용자의 특정 손바닥 정보를 삭제하는 API
     *
     * @param userId 사용자의 ID
     * @param palmprintId 삭제할 손바닥 정보의 ID
     */
    @DELETE("/api/admin/users/{user_id}/palmprints/{palmprint_id}")
    suspend fun deletePalmprint(
        @Path("user_id") userId: Int,
        @Path("palmprint_id") palmprintId: Int
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
     * 특정 디바이스의 정보를 수정하는 API (부분 수정)
     *
     * @param deviceId 수정할 디바이스의 ID
     * @param request 수정할 디바이스 정보
     * @return 수정된 디바이스의 전체 정보
     */
    @PATCH("/api/admin/devices/{device_id}")
    suspend fun updateDevice(
        @Path("device_id") deviceId: Int,
        @Body request: UpdateDeviceRequest
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
     * @param userId 특정 사용자 ID로 필터링 (optional)
     * @param status 인증 상태로 필터링 (optional)
     * @param sort 정렬 기준 (optional)
     * @return 인증 내역 목록과 페이지 정보
     */
    @GET("/api/admin/verifications")
    suspend fun getVerificationList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("user_id") userId: Int? = null,
        @Query("status") status: String? = null,
        @Query("sort") sort: String? = null
    ): VerificationListResponse

    /**
     * 전체 신고 내역을 페이지네이션하여 조회하는 API
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @param status 상태 필터 (optional)
     * @param userId 특정 사용자 ID로 필터링 (optional)
     * @param sort 정렬 기준 (optional)
     * @return 신고 내역 목록과 페이지 정보
     */
    @GET("/api/admin/reports")
    suspend fun getReportList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("status") status: String? = null,
        @Query("user_id") userId: String? = null,
        @Query("sort") sort: String? = null
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
    ): UpdateReportStatusResponse

}
