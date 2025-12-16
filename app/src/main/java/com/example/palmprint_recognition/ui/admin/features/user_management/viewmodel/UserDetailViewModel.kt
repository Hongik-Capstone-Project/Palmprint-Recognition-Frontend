package com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.AdminUserDetail
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<AdminUserDetail>>(UiState.Idle)
    val state = _state.asStateFlow()

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _state.value = UiState.Loading

            runCatching {
                adminRepository.getUserById(userId)
            }.onSuccess { result ->
                _state.value = UiState.Success(result)
            }.onFailure { e ->
                _state.value = UiState.Error(e.message ?: "오류가 발생했습니다.")
            }
        }
    }
}
