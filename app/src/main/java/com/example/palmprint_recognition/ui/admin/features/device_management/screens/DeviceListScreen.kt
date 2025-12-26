package com.example.palmprint_recognition.ui.admin.features.device_management.screens

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
import com.example.palmprint_recognition.data.model.AdminInstitution
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel.DeviceListViewModel
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.PaginationUiState

@Composable
fun DeviceListScreen(
    onDeviceClick: (Int) -> Unit,
    onRegisterDeviceClick: () -> Unit,
    viewModel: DeviceListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.refresh()
        }
    }

    DeviceListContent(
        uiState = uiState,
        onDeviceClick = onDeviceClick,
        onRegisterDeviceClick = onRegisterDeviceClick,
        onLoadMore = { viewModel.loadNextPage() }
    )
}

@Composable
private fun DeviceListContent(
    uiState: PaginationUiState<DeviceInfo>,
    onDeviceClick: (Int) -> Unit,
    onRegisterDeviceClick: () -> Unit,
    onLoadMore: () -> Unit
) {
    RootLayoutWeighted(
        headerWeight = 2.5f,
        bodyWeight = 6f,
        footerWeight = 2f,
        header = {
            HeaderContainer()
        },
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

@Composable
private fun DeviceListTableSection(
    uiState: PaginationUiState<DeviceInfo>,
    onDeviceClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        TableColumn(title = "device_id", width = 80),
        TableColumn(title = "기관명", weight = 1f),
        TableColumn(title = "위치", weight = 1f)
    )

    TableView(
        title = "디바이스 목록",
        columns = columns,
        rows = uiState.items.map { device ->
            listOf(
                device.id.toString(),
                device.institution.name, // 변경
                device.location
            )
        },
        hasMoreData = uiState.hasMore,
        isLoading = uiState.isLoadingInitial || uiState.isLoadingMore,
        modifier = modifier.fillMaxWidth(),
        onRowClick = { index -> onDeviceClick(uiState.items[index].id) },
        onLoadMore = onLoadMore
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDeviceListScreen() {
    val mockState = PaginationUiState(
        items = listOf(
            DeviceInfo(
                id = 1,
                createdAt = "2025-12-06T09:55:50.741Z",
                institution = AdminInstitution(
                    id = 10,
                    createdAt = "2025-12-01T00:00:00.000Z",
                    name = "홍익대학교",
                    address = null
                ),
                location = "T동 3층"
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
