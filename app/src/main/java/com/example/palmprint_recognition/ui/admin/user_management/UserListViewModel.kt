package com.example.palmprint_recognition.ui.admin.user_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 사용자 목록 화면(UserListScreen)의 ViewModel.
 *
 * 역할:
 * - AdminRepository 를 통해 전체 사용자 목록을 조회한다.
 * - 가져온 데이터를 UiState 형태로 화면에 전달한다.
 * - 화면에서는 userListState 를 collect 하여 UI 를 업데이트한다.
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    /**
     * _userListState
     * - 내부에서만 변경 가능한 MutableStateFlow
     * - UiState<List<AdminUserInfo>> 형태로 사용자 목록 상태를 저장한다.
     *
     * 외부에는 읽기 전용 형태(asStateFlow)로 제공하여
     * 화면에서 collectAsStateWithLifecycle() 로 구독하도록 한다.
     */
    private val _userListState =
        MutableStateFlow<UiState<List<AdminUserInfo>>>(UiState.Loading)

    val userListState = _userListState.asStateFlow()

    init {
        // ViewModel 생성 시 기본으로 사용자 목록을 로딩한다.
        loadUserList()
    }

    /**
     * 사용자 목록을 서버에서 조회하는 함수.
     *
     * @param page 페이지 번호(기본 1)
     * @param size 페이지당 항목 수(기본 10)
     */
    fun loadUserList(
        page: Int = 1,
        size: Int = 10
    ) {
        viewModelScope.launch {
            _userListState.value = UiState.Loading

            try {
                val response = adminRepository.getUserList(
                    page = page,
                    size = size
                )
                _userListState.value = UiState.Success(response.items)
            } catch (e: Exception) {
                _userListState.value = UiState.Error(
                    e.message ?: "사용자 목록을 불러오는 중 오류가 발생했습니다."
                )
            }
        }
    }
}
