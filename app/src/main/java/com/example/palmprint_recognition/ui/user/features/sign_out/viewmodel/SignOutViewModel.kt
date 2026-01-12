package com.example.palmprint_recognition.ui.user.features.sign_out.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.local.PreferenceManager
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val prefs: PreferenceManager
) : ViewModel() {

    private val _signOutState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val signOutState: StateFlow<UiState<Unit>> = _signOutState.asStateFlow()

    fun deleteAccount() {
        if (_signOutState.value is UiState.Loading) return

        viewModelScope.launch {
            _signOutState.value = UiState.Loading
            try {
                // 1) 서버 회원탈퇴
                userRepository.deleteMe()

                // 2) 로컬 토큰/프로필 삭제
                prefs.clearAllAuth()

                _signOutState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                // ApiException이든 HttpException이든 여기서 앱이 죽지 않게 잡기
                _signOutState.value = UiState.Error(
                    message = e.message ?: "회원탈퇴 중 오류가 발생했습니다."
                )
            }
        }
    }

    fun clearState() {
        _signOutState.value = UiState.Idle
    }
}
