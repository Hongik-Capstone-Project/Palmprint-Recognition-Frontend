package com.example.palmprint_recognition.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * PreferenceManager
 * - access/refresh token + 만료시각 + (프로필카드용) 사용자 정보 저장
 *
 * 주의:
 * - role은 서버 JWT payload의 user.role 값이 "admin"/"user" 등으로 올 수 있으니,
 *   앱 내부에서는 "ADMIN"/"USER"로 정규화해서 저장하는 걸 권장합니다.
 */
@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("auth_pref", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"

        // access token 만료 시각 (epoch seconds)
        private const val KEY_ACCESS_EXPIRES_AT = "access_expires_at"

        // 프로필 카드용 정보
        private const val KEY_ROLE = "role"       // "ADMIN" or "USER"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_USER_ID = "user_id"
    }

    // ----------------------------
    // Tokens
    // ----------------------------

    /**
     * @param expiresInSeconds 서버의 expires_in(초 단위)
     * 저장은 "만료 시각(epoch seconds)"로 저장합니다.
     */
    fun saveTokens(access: String, refresh: String, expiresInSeconds: Long) {
        val nowEpochSeconds = System.currentTimeMillis() / 1000L
        val expiresAt = nowEpochSeconds + expiresInSeconds

        prefs.edit()
            .putString(KEY_ACCESS, access)
            .putString(KEY_REFRESH, refresh)
            .putLong(KEY_ACCESS_EXPIRES_AT, expiresAt)
            .apply()
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH, null)

    fun getAccessExpiresAt(): Long = prefs.getLong(KEY_ACCESS_EXPIRES_AT, 0L)

    fun isAccessTokenExpired(): Boolean {
        val expiresAt = getAccessExpiresAt()
        if (expiresAt == 0L) return true
        val nowEpochSeconds = System.currentTimeMillis() / 1000L
        return nowEpochSeconds >= expiresAt
    }

    /**
     * access token이 존재하고 만료 전이면 로그인 상태로 봅니다.
     * (refresh 재발급 API가 없으므로 만료되면 사실상 재로그인이 필요)
     */
    fun isLoggedIn(): Boolean {
        val token = getAccessToken()
        return token != null && !isAccessTokenExpired()
    }

    // ----------------------------
    // Profile
    // ----------------------------

    data class UserProfile(
        val userId: String,
        val name: String,
        val email: String,
        val role: String // "ADMIN" or "USER"
    )

    fun saveProfile(userId: String, name: String, email: String, role: String) {
        prefs.edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun getProfile(): UserProfile? {
        val userId = prefs.getString(KEY_USER_ID, null) ?: return null
        val name = prefs.getString(KEY_NAME, null) ?: return null
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val role = prefs.getString(KEY_ROLE, null) ?: return null
        return UserProfile(userId, name, email, role)
    }

    fun getRole(): String? = prefs.getString(KEY_ROLE, null)

    // ----------------------------
    // Clear
    // ----------------------------

    fun clearAllAuth() {
        prefs.edit()
            .remove(KEY_ACCESS)
            .remove(KEY_REFRESH)
            .remove(KEY_ACCESS_EXPIRES_AT)
            .remove(KEY_USER_ID)
            .remove(KEY_NAME)
            .remove(KEY_EMAIL)
            .remove(KEY_ROLE)
            .apply()
    }
}
