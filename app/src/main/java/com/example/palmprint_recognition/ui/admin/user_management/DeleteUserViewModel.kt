package com.example.palmprint_recognition.ui.admin.user_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DeleteUserViewModel
 *
 * - 특정 사용자(userId)를 삭제하는 API(deleteUser)를 호출한다.
 * - 삭제 성공/실패 여부를 UiState로 관리하여 화면에 전달한다.
 */
@HiltViewModel
class DeleteUserViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /** 삭제 결과 상태를 나타내는 StateFlow */
    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteState = _deleteState.asStateFlow()

    /**
     * 사용자를 삭제하는 함수
     */
    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                repository.deleteUser(userId)
                _deleteState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _deleteState.value = UiState.Error(e.message)
            }
        }
    }
}
