package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.api.AdminApi
import com.example.palmprint_recognition.data.model.AddUserRequest
import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.model.DeviceListResponse
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.model.ErrorResponse
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
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

/**
 * 관리자 관련 API 호출을 담당하는 Repository
 * API 호출 시 발생하는 예외를 [ApiException]으로 변환하여 throw 합니다.
 */
class AdminRepository(
    private val adminApi: AdminApi,
    private val gson: Gson
) {

    /**
     * 전체 사용자 목록을 조회하는 API를 호출한다.
     * 성공 시 [UserListResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @param search 이름 또는 이메일 검색어 (optional)
     * @param sort 정렬 기준 (optional)
     * @return 서버에서 받은 사용자 목록과 페이지 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getUserList(
        page: Int = 1,
        size: Int = 10,
        search: String? = null,
        sort: String? = null
    ): UserListResponse {
        return try {
            adminApi.getUserList(page, size, search, sort)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 사용자 ID로 사용자 정보를 조회하는 API를 호출한다.
     * 성공 시 [AdminUserInfo]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 서버에서 받은 특정 사용자 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getUserById(userId: Int): AdminUserInfo {
        return try {
            adminApi.getUserById(userId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 관리자가 새로운 사용자를 추가하는 API를 호출한다.
     * 성공 시 [AddUserResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 서버에서 받은 생성된 사용자의 ID
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun addUser(name: String, email: String, password: String): AddUserResponse {
        return try {
            val request = AddUserRequest(name, email, password)
            adminApi.addUser(request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 관리자가 특정 사용자의 정보를 수정하는 API를 호출한다. (부분 수정)
     * 성공 시 [AdminUserInfo]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param userId 수정할 사용자의 ID
     * @param name 수정할 사용자 이름 (optional)
     * @param email 수정할 사용자 이메일 (optional)
     * @return 서버에서 받은 수정된 사용자의 전체 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun updateUser(userId: Int, name: String? = null, email: String? = null): AdminUserInfo {
        return try {
            val request = UpdateUserRequest(name, email)
            adminApi.updateUser(userId, request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 관리자가 특정 사용자를 삭제하는 API를 호출한다.
     * 성공 시 Unit을 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param userId 삭제할 사용자의 ID
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun deleteUser(userId: Int) {
        try {
            adminApi.deleteUser(userId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 사용자의 손바닥 정보 목록을 조회하는 API를 호출한다.
     * 성공 시 [PalmprintListResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 서버에서 받은 해당 사용자의 손바닥 정보 리스트
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getUserPalmprints(userId: Int): PalmprintListResponse {
        return try {
            adminApi.getUserPalmprints(userId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 손바닥 정보 파일을 업로드하는 API를 호출한다.
     * 성공 시 [PalmprintUploadResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param imageFile 업로드할 이미지 파일
     * @return 서버에서 받은 업로드 결과 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun uploadPalmprint(imageFile: File): PalmprintUploadResponse {
        return try {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("palmprint", imageFile.name, requestFile)

            adminApi.uploadPalmprint(body)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 사용자의 특정 손바닥 정보를 삭제하는 API를 호출한다.
     * 성공 시 Unit을 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param userId 사용자의 ID
     * @param palmprintId 삭제할 손바닥 정보의 ID
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun deletePalmprint(userId: Int, palmprintId: Int) {
        try {
            adminApi.deletePalmprint(userId, palmprintId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 전체 디바이스 목록을 조회하는 API를 호출한다.
     * 성공 시 [DeviceListResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @return 서버에서 받은 디바이스 목록과 페이지 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getDeviceList(
        page: Int = 1,
        size: Int = 10
    ): DeviceListResponse {
        return try {
            adminApi.getDeviceList(page, size)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 디바이스 ID로 디바이스 정보를 조회하는 API를 호출한다.
     * 성공 시 [DeviceInfo]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param deviceId 조회할 디바이스의 ID
     * @return 서버에서 받은 특정 디바이스 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getDeviceById(deviceId: Int): DeviceInfo {
        return try {
            adminApi.getDeviceById(deviceId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 디바이스의 정보를 수정하는 API를 호출한다. (부분 수정)
     * 성공 시 [DeviceInfo]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param deviceId 수정할 디바이스의 ID
     * @param identifier 수정할 디바이스 식별자 (optional)
     * @param memo 수정할 메모 (optional)
     * @return 서버에서 받은 수정된 디바이스의 전체 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun updateDevice(deviceId: Int, identifier: String? = null, memo: String? = null): DeviceInfo {
        return try {
            val request = UpdateDeviceRequest(identifier, memo)
            adminApi.updateDevice(deviceId, request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 새로운 디바이스를 등록하는 API를 호출한다.
     * 성공 시 [DeviceInfo]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param identifier 디바이스 식별자
     * @param memo 메모
     * @return 서버에서 받은 등록된 디바이스의 전체 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun registerDevice(identifier: String, memo: String): DeviceInfo {
        return try {
            val request = RegisterDeviceRequest(identifier, memo)
            adminApi.registerDevice(request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 디바이스를 삭제하는 API를 호출한다.
     * 성공 시 Unit을 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param deviceId 삭제할 디바이스의 ID
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun deleteDevice(deviceId: Int) {
        try {
            adminApi.deleteDevice(deviceId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 전체 인증 내역을 조회하는 API를 호출한다.
     * 성공 시 [VerificationListResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @param userId 특정 사용자 ID로 필터링 (optional)
     * @param status 인증 상태로 필터링 (optional)
     * @param sort 정렬 기준 (optional)
     * @return 서버에서 받은 인증 내역 목록과 페이지 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getVerificationList(
        page: Int = 1,
        size: Int = 10,
        userId: Int? = null,
        status: String? = null,
        sort: String? = null
    ): VerificationListResponse {
        return try {
            adminApi.getVerificationList(page, size, userId, status, sort)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 전체 신고 내역을 조회하는 API를 호출한다.
     * 성공 시 [ReportListResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지당 항목 수
     * @param status 상태 필터 (optional)
     * @param userId 특정 사용자 ID로 필터링 (optional)
     * @param sort 정렬 기준 (optional)
     * @return 서버에서 받은 신고 내역 목록과 페이지 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getReportList(
        page: Int = 1,
        size: Int = 10,
        status: String? = null,
        userId: String? = null,
        sort: String? = null
    ): ReportListResponse {
        return try {
            adminApi.getReportList(page, size, status, userId, sort)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 신고 내역을 ID로 조회하는 API를 호출한다.
     * 성공 시 [ReportInfo]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param reportId 조회할 신고 내역의 ID
     * @return 서버에서 받은 특정 신고 내역 정보
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun getReportById(reportId: Int): ReportInfo {
        return try {
            adminApi.getReportById(reportId)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 특정 신고 내역의 상태를 수정하는 API를 호출한다.
     * 성공 시 [UpdateReportStatusResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param reportId 수정할 신고 내역의 ID
     * @param status 변경할 상태 ("approved" 또는 "rejected")
     * @return 서버에서 받은 수정된 신고 내역 정보와 성공 메시지
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun updateReportStatus(reportId: Int, status: String): UpdateReportStatusResponse {
        return try {
            val request = UpdateReportStatusRequest(status)
            adminApi.updateReportStatus(reportId, request)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

}
