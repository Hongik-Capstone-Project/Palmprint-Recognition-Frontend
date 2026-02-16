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
    val totalCount: Int? = null,       // 등록된 손바닥 개수
    val errorMessage: String? = null
)

@HiltViewModel
class UserMainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _palmStatus = MutableStateFlow(PalmprintStatusUiState())
    val palmStatus = _palmStatus.asStateFlow()

    private var inFlight = false
    private var hasLoadedOnce = false

    /**
     * force=false: 최초 1회만 GET
     * force=true: 등록/삭제 이후 갱신(요구사항)
     */
    fun refreshPalmCount(force: Boolean = false) {
        if (inFlight) return
        if (!force && hasLoadedOnce) return

        viewModelScope.launch {
            inFlight = true
            _palmStatus.value = _palmStatus.value.copy(isLoading = true, errorMessage = null)

            runCatching {
                userRepository.getMyPalms()
            }.onSuccess { res ->
                hasLoadedOnce = true
                _palmStatus.value = PalmprintStatusUiState(
                    isLoading = false,
                    totalCount = res.totalCount,
                    errorMessage = null
                )
            }.onFailure { e ->
                // 실패해도 앱이 죽지 않게: totalCount는 null 유지
                _palmStatus.value = PalmprintStatusUiState(
                    isLoading = false,
                    totalCount = null,
                    errorMessage = e.message
                )
            }

            inFlight = false
        }
    }

    /**
     * (선택) 다른 화면에서 totalCount를 이미 알고 있을 때 주입 가능
     * - 예: Register 화면에서 등록 후 GET까지 해서 totalCount를 알고 있음
     */
    fun setPalmCount(count: Int) {
        hasLoadedOnce = true
        _palmStatus.value = PalmprintStatusUiState(isLoading = false, totalCount = count)
    }
}