package com.example.palmprint_recognition.ui.admin.features.user_management.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel.UserListViewModel
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.PaginationUiState

/**
 * 유저 목록 Screen
 *
 * @param onUserClick 유저 클릭 시 상세 화면 이동
 * @param onAddUserClick 유저 추가 버튼 클릭 이벤트
 * @param viewModel UserListViewModel
 */
@Composable
fun UserListScreen(
    onUserClick: (Int) -> Unit,
    onAddUserClick: () -> Unit,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.refresh()
        }
    }

    UserListContent(
        uiState = uiState,
        onUserClick = onUserClick,
        onAddUserClick = onAddUserClick,
        onLoadMore = {
            viewModel.loadNextPage()
        }
    )
}

/**
 * 유저 목록 화면 UI
 *
 * @param uiState 유저 목록 Pagination 상태
 * @param onUserClick 유저 클릭 이벤트
 * @param onAddUserClick 유저 추가 버튼 클릭 이벤트
 * @param onLoadMore 다음 페이지 로드 이벤트
 */
@Composable
private fun UserListContent(
    uiState: PaginationUiState<AdminUserInfo>,
    onUserClick: (Int) -> Unit,
    onAddUserClick: () -> Unit,
    onLoadMore: () -> Unit
) {
    RootLayout(

        // 화면 비율 설정
        headerWeight = 2f,
        bodyWeight = 6f,
        footerWeight = 2f,
        sectionGapWeight = 0.4f,

        // ===============================
        // HEADER
        // ===============================
        header = {
            Header(
                userName = "Alice",
                userEmail = "alice@example.com"
            )
        },

        // ===============================
        // BODY
        // ===============================
        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                UserListTableSection(
                    uiState = uiState,
                    onUserClick = onUserClick,
                    onLoadMore = onLoadMore,
                    modifier = Modifier.weight(1f)
                )
            }
        },

        // ===============================
        // FOOTER
        // ===============================
        footer = {
            Footer {
                SingleCenterButton(
                    text = "유저 정보 추가",
                    onClick = onAddUserClick
                )
            }
        }
    )
}

/**
 * 유저 목록 테이블 Section
 *
 * @param uiState Pagination 상태
 * @param onUserClick 유저 클릭 이벤트
 * @param onLoadMore 다음 페이지 로드
 * @param modifier 외부 레이아웃 제어용 Modifier
 */
@Composable
private fun UserListTableSection(
    uiState: PaginationUiState<AdminUserInfo>,
    onUserClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        TableColumn(
            title = "user_id",
            width = 70
        ),
        TableColumn(
            title = "email",
            weight = 1f
        )
    )

    TableView(
        title = "유저 목록",
        columns = columns,
        rows = uiState.items.map { user ->
            listOf(
                user.id.toString(),
                user.email
            )
        },
        hasMoreData = uiState.hasMore,
        isLoading = uiState.isLoadingInitial || uiState.isLoadingMore,
        modifier = modifier.fillMaxWidth(),
        onRowClick = { index ->
            onUserClick(uiState.items[index].id)
        },
        onLoadMore = onLoadMore
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewUserListScreen() {
    val mockState = PaginationUiState(
        items = listOf(
            AdminUserInfo(
                id = 1,
                createdAt = "2025-12-06T09:55:50.741Z",
                email = "alice@example.com",
                name = "Alice"
            ),
            AdminUserInfo(
                id = 2,
                createdAt = "2025-12-07T11:20:00.000Z",
                email = "bob@example.com",
                name = "Bob"
            )
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
