package com.example.palmprint_recognition.ui.auth.features.logout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.local.PreferenceManager
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val prefs: PreferenceManager
) : ViewModel() {

    private val _logoutState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val logoutState: StateFlow<UiState<Unit>> = _logoutState

    fun logoutLocal() {
        viewModelScope.launch {
            _logoutState.value = UiState.Loading
            try {
                prefs.clearAllAuth()
                _logoutState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _logoutState.value = UiState.Error(e.message ?: "로그아웃 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _logoutState.value = UiState.Idle
    }
}
