package com.example.palmprint_recognition.ui.admin.user_management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.ui.admin.common.PaginationUiState
import com.example.palmprint_recognition.ui.common.components.PrimaryButton
import com.example.palmprint_recognition.ui.common.components.ProfileCard
import com.example.palmprint_recognition.ui.common.components.LogoPalmAI
import com.example.palmprint_recognition.ui.common.components.table.TableColumn
import com.example.palmprint_recognition.ui.common.components.table.TableView
import com.example.palmprint_recognition.ui.common.theme.Typography

/**
 * UserListScreen (ViewModel 연결)
 */
@Composable
fun UserListScreen(
    onUserClick: (Int) -> Unit,
    onAddUserClick: () -> Unit,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserListContent(
        uiState = uiState,
        onUserClick = onUserClick,
        onAddUserClick = onAddUserClick,
        onLoadMore = { viewModel.loadNextPage() }
    )
}

/**
 * UserList 화면 UI (preview 가능)
 */
@Composable
fun UserListContent(
    uiState: PaginationUiState<AdminUserInfo>,
    onUserClick: (Int) -> Unit,
    onAddUserClick: () -> Unit,
    onLoadMore: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // -----------------------
        // 로고
        // -----------------------
        LogoPalmAI(
            modifier = Modifier.offset(x = 20.dp, y = 0.dp)
        )

        // -----------------------
        // 프로필 카드
        // -----------------------
        ProfileCard(
            name = "OO",
            email = "email@example.com",
            modifier = Modifier
                .offset(x = 17.dp, y = 89.dp)
                .width(368.dp)
                .height(48.dp)
        )

        // -----------------------
        // 타이틀
        // -----------------------
        Text(
            text = "유저 목록",
            style = Typography.titleMedium.copy(
                fontSize = 14.sp,
                color = Color(0xFF21272A)
            ),
            modifier = Modifier.offset(x = 28.dp, y = 189.dp)
        )

        // -----------------------
        // TableView
        // -----------------------
        val columns = listOf(
            TableColumn("user_id", width = 60),
            TableColumn("email", weight = 1f)
        )

        TableView(
            columns = columns,
            rows = uiState.items.map { user ->
                listOf(user.id.toString(), user.email)
            },
            hasMoreData = uiState.hasMore,
            isLoading = uiState.isLoadingMore,
            modifier = Modifier.offset(x = 25.dp, y = 222.dp),
            onRowClick = { index ->
                val userId = uiState.items[index].id
                onUserClick(userId)
            },
            onLoadMore = onLoadMore
        )

        // -----------------------
        // 유저 추가 버튼
        // -----------------------
        PrimaryButton(
            text = "유저 정보 추가",
            onClick = onAddUserClick,
            modifier = Modifier
                .offset(x = 28.dp, y = 760.dp)
                .width(356.dp)
                .height(56.dp)
        )
    }
}

/**
 * Preview
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewUserListScreen() {
    val mockState = PaginationUiState(
        items = listOf(
            AdminUserInfo(1, "Alice", "alice@example.com"),
            AdminUserInfo(2, "Bob", "bob@example.com")
        ),
        isLoadingInitial = false,
        isLoadingMore = false,
        hasMore = true
    )

    UserListContent(
        uiState = mockState,
        onUserClick = {},
        onAddUserClick = {},
        onLoadMore = {}
    )
}
