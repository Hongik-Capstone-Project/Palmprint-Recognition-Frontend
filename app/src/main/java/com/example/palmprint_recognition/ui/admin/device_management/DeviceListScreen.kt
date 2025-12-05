package com.example.palmprint_recognition.ui.admin.device_management

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
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.common.PaginationUiState
import com.example.palmprint_recognition.ui.common.components.LogoPalmAI
import com.example.palmprint_recognition.ui.common.components.PrimaryButton
import com.example.palmprint_recognition.ui.common.components.ProfileCard
import com.example.palmprint_recognition.ui.common.components.table.TableColumn
import com.example.palmprint_recognition.ui.common.components.table.TableView
import com.example.palmprint_recognition.ui.common.theme.Typography

/**
 * ✔ DeviceListScreen (ViewModel 연결)
 */
@Composable
fun DeviceListScreen(
    onDeviceClick: (Int) -> Unit,
    onAddDeviceClick: () -> Unit,
    viewModel: DeviceListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeviceListContent(
        uiState = uiState,
        onDeviceClick = onDeviceClick,
        onAddDeviceClick = onAddDeviceClick,
        onLoadMore = { viewModel.loadNextPage() }
    )
}

/**
 * ✔ 실제 UI 그리기 (preview 가능)
 * ✔ UserListContent와 거의 동일하되 텍스트/컬럼만 다름
 */
@Composable
fun DeviceListContent(
    uiState: PaginationUiState<DeviceInfo>,
    onDeviceClick: (Int) -> Unit,
    onAddDeviceClick: () -> Unit,
    onLoadMore: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // 1) 로고
        LogoPalmAI(modifier = Modifier.offset(x = 20.dp, y = 0.dp))

        // 2) 프로필 카드
        ProfileCard(
            name = "OO",
            email = "email@example.com",
            modifier = Modifier
                .offset(x = 17.dp, y = 89.dp)
                .width(368.dp)
                .height(48.dp)
        )

        // 3) 타이틀
        Text(
            text = "디바이스 목록",
            style = Typography.titleMedium.copy(fontSize = 14.sp),
            modifier = Modifier.offset(x = 28.dp, y = 189.dp)
        )

        // 4) 테이블
        val tableColumns = listOf(
            TableColumn("device_id", width = 80),
            TableColumn("기관명", weight = 1f),
            TableColumn("위치", weight = 1f)
        )

        TableView(
            columns = tableColumns,
            rows = uiState.items.map { device ->
                listOf(
                    device.id.toString(),
                    device.identifier,  // TODO(): api 수정 후 기관명으로 변경
                    device.memo         // TODO(): api 수정 후 위치로 변경
                )
            },
            hasMoreData = uiState.hasMore,
            isLoading = uiState.isLoadingMore,
            modifier = Modifier.offset(x = 25.dp, y = 222.dp),
            onRowClick = { index ->
                val deviceId = uiState.items[index].id
                onDeviceClick(deviceId)
            },
            onLoadMore = onLoadMore
        )

        // 5) 하단 버튼
        PrimaryButton(
            text = "디바이스 추가",
            onClick = onAddDeviceClick,
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
fun PreviewDeviceListScreen() {
    val mockState = PaginationUiState(
        items = listOf(
            DeviceInfo(1, "기관명1", "위치1", createdAt = "2025-03-01"),
            DeviceInfo(2, "기관명2", "위치2", createdAt = "2025-03-02")
        ),
        isLoadingInitial = false,
        isLoadingMore = false,
        hasMore = true
    )

    DeviceListContent(
        uiState = mockState,
        onDeviceClick = {},
        onAddDeviceClick = {},
        onLoadMore = {}
    )
}
