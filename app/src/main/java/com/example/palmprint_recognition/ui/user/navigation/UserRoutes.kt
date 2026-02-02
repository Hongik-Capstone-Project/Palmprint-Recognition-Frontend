package com.example.palmprint_recognition.ui.user.navigation

/**
 * 사용자(User) 기능의 네비게이션 Route 를 모아둔 객체.
 */
object UserRoutes {

    /** 사용자 메인 화면 */
    const val MAIN = "user_main"

    /** 회원탈퇴 확인 화면  */
    const val DELETE_ACCOUNT = "delete_account"

    const val LOGOUT = "user_logout"

    // Institution
    const val INSTITUTION_LIST = "user_institution_list"
    const val ADD_INSTITUTION = "user_add_institution"

    private const val ARG_INSTITUTION_ID = "institutionId"
    const val DELETE_INSTITUTION = "user_delete_institution/{$ARG_INSTITUTION_ID}"
    fun deleteInstitution(institutionId: Int): String = "user_delete_institution/$institutionId"

    // Payment
    const val PAYMENT_LIST = "user_payment_list"
    const val ADD_PAYMENT = "user_add_payment"

    private const val ARG_PAYMENT_METHOD_ID = "paymentMethodId"
    const val DELETE_PAYMENT = "user_delete_payment/{$ARG_PAYMENT_METHOD_ID}"
    fun deletePayment(paymentMethodId: Int): String = "user_delete_payment/$paymentMethodId"

    // History
    const val HISTORY_LIST = "user_history_list"
    const val REPORT_HISTORY = "user_report_history"

    // Palmprint
    const val REGISTER_PALMPRINT = "user_register_palmprint"
    const val DELETE_PALMPRINT = "user_delete_palmprint"
}
