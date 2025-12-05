package com.example.palmprint_recognition.ui.admin.navigation

/**
 * AdminRoutes
 *
 * - AdminNavigation 에서 사용할 모든 네비게이션 Route 문자열을 중앙에서 관리한다.
 * - 화면 전환 시 문자열을 직접 입력하지 않도록 하여 오타 오류를 방지한다.
 */
object AdminRoutes {

    /* ------------------------------------------------------------------------
     * Dashboard
     * ------------------------------------------------------------------------ */
    const val DASHBOARD = "admin_dashboard"

    /* ------------------------------------------------------------------------
     * USER MANAGEMENT
     * ------------------------------------------------------------------------ */
    const val USER_LIST = "admin_user_list"

    const val USER_DETAIL = "admin_user_detail/{userId}"
    fun userDetail(userId: Int) = "admin_user_detail/$userId"

    const val ADD_USER = "admin_add_user"

    const val DELETE_USER = "admin_delete_user/{userId}"
    fun deleteUser(userId: Int) = "admin_delete_user/$userId"


    /* ------------------------------------------------------------------------
     * DEVICE MANAGEMENT
     * ------------------------------------------------------------------------ */
    const val DEVICE_LIST = "admin_device_list"

    const val DEVICE_DETAIL = "admin_device_detail/{deviceId}"
    fun deviceDetail(deviceId: Int) = "admin_device_detail/$deviceId"

    const val ADD_DEVICE = "admin_add_device"

    const val DELETE_DEVICE = "admin_delete_device/{deviceId}"
    fun deleteDevice(deviceId: Int) = "admin_delete_device/$deviceId"

    /* ------------------------------------------------------------------------
     * REPORT MANAGEMENT
     * ------------------------------------------------------------------------ */
    const val REPORT_LIST = "admin_report_list"

    const val REPORT_DETAIL = "admin_report_detail/{reportId}"
    fun reportDetail(reportId: Int) = "admin_report_detail/$reportId"

    /* ------------------------------------------------------------------------
     * VERIFICATION MANAGEMENT (인증 내역 조회)
     * ------------------------------------------------------------------------ */
    const val VERIFICATION_LIST = "admin_verification_list"
}
