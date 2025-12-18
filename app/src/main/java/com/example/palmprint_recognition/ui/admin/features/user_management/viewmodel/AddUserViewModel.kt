package com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _addUserState = MutableStateFlow<UiState<AddUserResponse>>(UiState.Idle)
    val addUserState = _addUserState.asStateFlow()

    fun addUser(
        name: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _addUserState.value = UiState.Loading

            runCatching {
                adminRepository.addUser(
                    name = name,
                    email = email,
                    password = password
                )
            }.onSuccess { result ->
                _addUserState.value = UiState.Success(result)
            }.onFailure { e ->
                _addUserState.value = UiState.Error(e.message ?: "유저 생성 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _addUserState.value = UiState.Idle
    }
}
