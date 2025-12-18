package com.example.palmprint_recognition.ui.admin.features.device_management.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel.DeviceListViewModel
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.PaginationUiState

/**
 * 디바이스 목록 Screen
 *
 * @param onDeviceClick 디바이스 클릭 시 상세 화면 이동
 * @param onRegisterDeviceClick 디바이스 추가 버튼 클릭 이벤트
 * @param viewModel UserListViewModel
 */
@Composable
fun DeviceListScreen(
    onDeviceClick: (Int) -> Unit,
    onRegisterDeviceClick: () -> Unit,
    viewModel: DeviceListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeviceListContent(
        uiState = uiState,
        onDeviceClick = onDeviceClick,
        onRegisterDeviceClick = onRegisterDeviceClick,
        onLoadMore = {
            viewModel.loadNextPage()
        }
    )
}

/**
 * 디바이스 목록 화면 UI
 *
 * @param uiState 디바이스 목록 Pagination 상태
 * @param onDeviceClick 디바이스 클릭 이벤트
 * @param onRegisterDeviceClick 디바이스 추가 버튼 클릭 이벤트
 * @param onLoadMore 다음 페이지 로드 이벤트
 */
@Composable
private fun DeviceListContent(
    uiState: PaginationUiState<DeviceInfo>,
    onDeviceClick: (Int) -> Unit,
    onRegisterDeviceClick: () -> Unit,
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
            HeaderContainer()
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
                DeviceListTableSection(
                    uiState = uiState,
                    onDeviceClick = onDeviceClick,
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
                    text = "디바이스 정보 추가",
                    onClick = onRegisterDeviceClick
                )
            }
        }
    )
}

/**
 * 디바이스 목록 테이블 Section
 *
 * @param uiState Pagination 상태
 * @param onDeviceClick 디바이스 클릭 이벤트
 * @param onLoadMore 다음 페이지 로드
 * @param modifier 외부 레이아웃 제어용 Modifier
 */
@Composable
private fun DeviceListTableSection(
    uiState: PaginationUiState<DeviceInfo>,
    onDeviceClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        TableColumn(
            title = "device_id",
            width = 80
        ),
        TableColumn(
            title = "기관명",
            weight = 1f
        ),
        TableColumn(
            title = "위치",
            weight = 1f
        )
    )

    TableView(
        title = "디바이스 목록",
        columns = columns,
        rows = uiState.items.map { device ->
            listOf(
                device.id.toString(),
                device.institutionName,
                device.location
            )
        },
        hasMoreData = uiState.hasMore,
        isLoading = uiState.isLoadingInitial || uiState.isLoadingMore,
        modifier = modifier.fillMaxWidth(),
        onRowClick = { index ->
            onDeviceClick(uiState.items[index].id)
        },
        onLoadMore = onLoadMore
    )


}

/**
 * DeviceListScreen Preview
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDeviceListScreen() {
    val mockState = PaginationUiState(
        items = listOf(
            DeviceInfo(
                id = 1,
                institutionName = "홍익대학교",
                location = "T동 3층",
                createdAt = "2025-12-06T09:55:50.741Z"
            )
        ),
        isLoadingInitial = false,
        isLoadingMore = false,
        hasMore = true
    )

    DeviceListContent(
        uiState = mockState,
        onDeviceClick = {},
        onRegisterDeviceClick = {},
        onLoadMore = {}
    )
}
