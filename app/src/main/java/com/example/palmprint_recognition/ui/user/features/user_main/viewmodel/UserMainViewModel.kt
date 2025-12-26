package com.example.palmprint_recognition.ui.user.features.user_main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PalmprintStatusUiState(
    val isLoading: Boolean = false,
    val isRegistered: Boolean? = null, // null = 아직 모름/조회 실패
    val errorMessage: String? = null
)

@HiltViewModel
class UserMainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _palmStatus = MutableStateFlow(PalmprintStatusUiState())
    val palmStatus = _palmStatus.asStateFlow()

    private var inFlight = false

    fun refreshPalmprintStatus() {
        if (inFlight) return

        viewModelScope.launch {
            inFlight = true
            _palmStatus.value = _palmStatus.value.copy(
                isLoading = true,
                errorMessage = null
            )

            runCatching {
                userRepository.getPalmprintRegistrationStatus()
            }.onSuccess { res ->
                _palmStatus.value = PalmprintStatusUiState(
                    isLoading = false,
                    isRegistered = res.isPalmprintRegistered,
                    errorMessage = null
                )
            }.onFailure { e ->
                // 실패해도 앱이 죽지 않게: null로 두고 기본 문구 표시
                _palmStatus.value = PalmprintStatusUiState(
                    isLoading = false,
                    isRegistered = null,
                    errorMessage = e.message
                )
            }

            inFlight = false
        }
    }
}
