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
class InstitutionListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<UserInstitution>>>(UiState.Idle)
    val state = _state.asStateFlow()

    fun loadInstitutions() {
        viewModelScope.launch {
            _state.value = UiState.Loading

            runCatching {
                userRepository.getUserInstitutions()
            }.onSuccess { list ->
                _state.value = UiState.Success(list)
            }.onFailure { e ->
                _state.value = UiState.Error(e.message ?: "오류가 발생했습니다.")
            }
        }
    }
}
