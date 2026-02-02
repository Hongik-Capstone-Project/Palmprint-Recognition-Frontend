package com.example.palmprint_recognition.ui.admin.navigation

object AdminRoutes {

    const val DASHBOARD = "admin_dashboard"
    const val LOGOUT = "admin_logout"

    // USER
    const val USER_LIST = "admin_user_list"
    const val USER_DETAIL = "admin_user_detail/{userId}"
    const val ADD_USER = "admin_add_user"
    const val DELETE_USER = "admin_delete_user/{userId}"

    fun userDetail(userId: Int) = "admin_user_detail/$userId"
    fun deleteUser(userId: Int) = "admin_delete_user/$userId"

    // DEVICE
    const val DEVICE_LIST = "admin_device_list"
    const val DEVICE_DETAIL = "admin_device_detail/{deviceId}"
    const val REGISTER_DEVICE = "admin_register_device"
    const val DELETE_DEVICE = "admin_delete_device/{deviceId}"

    fun deviceDetail(deviceId: Int) = "admin_device_detail/$deviceId"
    fun deleteDevice(deviceId: Int) = "admin_delete_device/$deviceId"

    // REPORT
    const val REPORT_LIST = "admin_report_list"
    const val REPORT_DETAIL = "admin_report_detail/{reportId}"

    fun reportDetail(reportId: Int) = "admin_report_detail/$reportId"

    // VERIFICATION
    const val VERIFICATION = "admin_verification"

    //  args key 상수
    const val ARG_USER_ID = "userId"
    const val ARG_DEVICE_ID = "deviceId"
    const val ARG_REPORT_ID = "reportId"
}
