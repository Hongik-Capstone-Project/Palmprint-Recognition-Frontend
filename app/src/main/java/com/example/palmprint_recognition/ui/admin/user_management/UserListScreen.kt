package com.example.palmprint_recognition.ui.admin.user_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * UserListScreen
 *
 * 역할
 * - ViewModel(UserListViewModel)의 상태를 구독하여 로딩/성공/에러 UI를 표시한다.
 * - 각 유저 아이템 클릭 시 onUserClick 콜백을 호출한다.
 * - 화면 하단 "유저 정보 추가" 버튼 클릭 시 onAddUserClick 콜백을 호출한다.
 *
 * 중요한 점
 * - 이 화면 자체는 네비게이션에 직접 접근하지 않는다.
 * - 이동은 반드시 상위(AdminNavigation)에서 결정한다. (MVVM + Navigation 베스트 프랙티스)
 */
@Composable
fun UserListScreen(
    onUserClick: (Int) -> Unit,
    onAddUserClick: () -> Unit,
    viewModel: UserListViewModel = hiltViewModel()
) {
    // ViewModel의 StateFlow를 Compose State로 변환하여 구독
    val state by viewModel.userListState.collectAsStateWithLifecycle()

    // 실제 UI만 분리한 함수
    UserListContent(
        state = state,
        onUserClick = onUserClick,
        onAddUserClick = onAddUserClick
    )
}

/**
 * UserListScreen의 실제 화면 UI를 담당하는 함수.
 * ViewModel이 없기 때문에 Preview 테스트가 가능해진다.
 */
@Composable
fun UserListContent(
    state: UiState<List<AdminUserInfo>>,
    onUserClick: (Int) -> Unit,
    onAddUserClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        /** 1) 화면 제목 */
        Text(
            text = "유저 목록", // TODO: strings.xml 분리
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        /** 2) 상태에 따라 UI 분기 */
        when (state) {

            UiState.Idle -> {
                Text("유저 목록을 준비 중...")
            }

            UiState.Loading -> {
                Text("불러오는 중...")
            }

            is UiState.Error -> {
                Text(
                    text = state.message ?: "오류가 발생했습니다.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            is UiState.Success -> {
                val users = state.data

                /**
                 * LazyColumn:
                 * - 화면 세로 공간 중 가변 영역을 차지하기 위해 weight(1f)를 사용
                 * - 목록은 스크롤되고, 아래의 버튼은 고정됨
                 */
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(users, key = { it.id }) { user ->
                        UserItemRow(
                            user = user,
                            onClick = { onUserClick(user.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /**
         * 3) 유저 정보 추가 버튼
         *
         * 버튼 위치: Column 의 가장 아래
         * 역할: 상위에서 전달받은 onAddUserClick 콜백 실행
         * 실제 네비게이션은 AdminNavigation 에서 수행
         */
        Button(
            onClick = onAddUserClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("유저 정보 추가") // TODO: strings.xml
        }
    }
}

/**
 * 유저 한 명을 표시하는 단순 버튼 UI.
 * 클릭 시 onClick 콜백을 호출한다.
 */
@Composable
fun UserItemRow(
    user: AdminUserInfo,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(text = user.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = user.email, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/**
 * Preview: ViewModel 없이 렌더링 가능하도록 샘플 데이터 제공
 */
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewUserListScreen() {
    val sampleUsers = listOf(
        AdminUserInfo(1, "Alice", "alice@example.com"),
        AdminUserInfo(2, "Bob", "bob@example.com")
    )

    UserListContent(
        state = UiState.Success(sampleUsers),
        onUserClick = {},
        onAddUserClick = {}
    )
}
