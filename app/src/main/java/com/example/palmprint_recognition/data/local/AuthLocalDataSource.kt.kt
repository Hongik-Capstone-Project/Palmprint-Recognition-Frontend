package com.example.palmprint_recognition.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 앱 내부에 로그인 관련 정보를 저장/조회하는 Local DataSource.
 *
 * - SharedPreferences 를 사용해 간단히 구현한다.
 * - 나중에 DataStore 로 교체해도 인터페이스만 유지하면 됨.
 */
@Singleton
class AuthLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAuthInfo(token: String, role: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun clearAuthInfo() {
        prefs.edit()
            .clear()
            .apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getRole(): String? = prefs.getString(KEY_ROLE, null)

    fun isLoggedIn(): Boolean = getToken() != null

    companion object {
        private const val KEY_TOKEN = "key_token"
        private const val KEY_ROLE = "key_role" // "ADMIN" or "USER"
    }
}
