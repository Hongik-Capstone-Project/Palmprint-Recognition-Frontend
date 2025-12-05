package com.example.palmprint_recognition.ui.admin.verification

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
import com.example.palmprint_recognition.data.model.VerificationRecord
import com.example.palmprint_recognition.ui.admin.common.PaginationUiState
import com.example.palmprint_recognition.ui.common.components.LogoPalmAI
import com.example.palmprint_recognition.ui.common.components.ProfileCard
import com.example.palmprint_recognition.ui.common.components.table.TableColumn
import com.example.palmprint_recognition.ui.common.components.table.TableView
import com.example.palmprint_recognition.ui.common.theme.Typography

/**
 * ViewModel + 상태 연결
 */
@Composable
fun VerificationListScreen(
    viewModel: VerificationListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VerificationListContent(
        uiState = uiState,
        onLoadMore = { viewModel.loadNextPage() }
    )
}

/**
 * 순수 UI 전용 화면
 */
@Composable
fun VerificationListContent(
    uiState: PaginationUiState<VerificationRecord>,
    onLoadMore: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // 1) 로고
        LogoPalmAI(
            modifier = Modifier.offset(x = 20.dp, y = 0.dp)
        )

        // 2) 프로필 카드
        ProfileCard(
            name = "OO",
            email = "email",
            modifier = Modifier
                .offset(x = 17.dp, y = 89.dp)
                .width(368.dp)
                .height(48.dp)
        )

        // 3) 인증 통계 박스 (하드코딩)
        VerificationStatsBox(
            modifier = Modifier.offset(x = 17.dp, y = 185.dp)
        )

        // 4) 제목: 인증 통계 상세
        Text(
            text = "인증 통계 상세",
            style = Typography.titleMedium.copy(
                fontSize = 14.sp,
                color = Color(0xFF21272A)
            ),
            modifier = Modifier.offset(x = 25.dp, y = 379.dp)
        )

        // 5) 테이블
        val columns = listOf(
            TableColumn("user_id", width = 60),
            TableColumn("location", weight = 1f)
        )

        TableView(
            columns = columns,
            rows = uiState.items.map { record ->
                listOf(
                    record.user.id.toString(),
                    record.user.name  // 임시 location → API 정해지면 수정
                )
            },
            hasMoreData = uiState.hasMore,
            isLoading = uiState.isLoadingMore,
            modifier = Modifier.offset(x = 25.dp, y = 438.dp),

            onRowClick = { /* 상세 페이지 없음 */ },
            onLoadMore = onLoadMore
        )
    }
}

/**
 * 인증 통계 하드코딩 박스: TODO(): api 작성 완료 후 수정
 */
@Composable
fun VerificationStatsBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(377.dp)
            .height(160.dp)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column {
            Text("전체 유저 수: 00명", fontSize = 14.sp, color = Color(0xFF21272A))
            Spacer(Modifier.height(12.dp))

            Text("등록된 손바닥 수: 00개", fontSize = 14.sp, color = Color(0xFF21272A))
            Spacer(Modifier.height(12.dp))

            Text("인증 요청 수: 00건", fontSize = 14.sp, color = Color(0xFF21272A))
            Spacer(Modifier.height(12.dp))

            Text("인증 성공률: 00%", fontSize = 14.sp, color = Color(0xFF21272A))
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewVerificationListScreen() {
    val sample = PaginationUiState(
        items = listOf(
            VerificationRecord(
                id = "1",
                status = "approved",
                createdAt = "...",
                user = AdminUserInfo(1, "홍길동", "hong@example.com")
            ),
            VerificationRecord(
                id = "2",
                status = "rejected",
                createdAt = "...",
                user = AdminUserInfo(2, "김철수", "kim@example.com")
            ),
        ),
        isLoadingInitial = false,
        isLoadingMore = false,
        hasMore = true
    )

    VerificationListContent(
        uiState = sample,
        onLoadMore = {}
    )
}
