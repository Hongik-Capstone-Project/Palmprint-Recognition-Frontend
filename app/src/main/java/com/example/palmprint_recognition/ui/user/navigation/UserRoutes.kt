package com.example.palmprint_recognition.ui.user.navigation

/**
 * 사용자(User) 기능의 네비게이션 Route 를 모아둔 객체.
 *
 * - 모든 문자열 경로를 한 곳에서 관리하여 오타를 방지한다.
 */
object UserRoutes {

    /** 사용자 메인 화면 */
    const val MAIN = "user_main"

    /** 회원탈퇴 확인 화면 */
    const val DELETE_ACCOUNT = "delete_account"

    /** 결제 내역 화면 (추가 예정) */
    const val PAYMENT_LIST = "user_payment_list"

    /** 기관 목록 화면 (추가 예정) */
    const val INSTITUTION_LIST = "user_institution_list"

    /** 히스토리(출입 기록 등) */
    const val HISTORY_LIST = "user_history_list"
}
