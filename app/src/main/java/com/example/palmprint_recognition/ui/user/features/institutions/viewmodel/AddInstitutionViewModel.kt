package com.example.palmprint_recognition.ui.user.features.institutions.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.UserInstitution
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddInstitutionViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _addInstitutionState =
        MutableStateFlow<UiState<UserInstitution>>(UiState.Idle)
    val addInstitutionState = _addInstitutionState.asStateFlow()

    fun addInstitution(institutionId: Int, institutionUserId: String) {
        viewModelScope.launch {
            _addInstitutionState.value = UiState.Loading

            runCatching {
                userRepository.addUserInstitution(
                    institutionId = institutionId,
                    institutionUserId = institutionUserId
                )
            }.onSuccess { result ->
                _addInstitutionState.value = UiState.Success(result)
            }.onFailure { e ->
                _addInstitutionState.value =
                    UiState.Error(e.message ?: "인증기관 추가 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _addInstitutionState.value = UiState.Idle
    }
}
