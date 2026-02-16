package com.example.palmprint_recognition.ui.user.features.palmprint_management.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.data.model.PalmInfo
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.common.screens.ConfirmYesNoScreen
import com.example.palmprint_recognition.ui.common.screens.ResultScreen
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.palmprint_management.viewmodel.DeletePalmprintViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private enum class DeleteMode { NONE, DELETE_ONE, DELETE_ALL }

@Composable
fun DeletePalmprintScreen(
    onGoMain: () -> Unit,
    onCancel: () -> Unit, // Confirm에서 사용
    viewModel: DeletePalmprintViewModel = hiltViewModel()
) {
    val palmsState by viewModel.palmsState.collectAsStateWithLifecycle()
    val actionState by viewModel.actionState.collectAsStateWithLifecycle()

    var deleteMode by remember { mutableStateOf(DeleteMode.NONE) }
    var selectedPalm by remember { mutableStateOf<PalmInfo?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadMyPalms()
    }

    // 성공이면 결과 팝업
    if (actionState is UiState.Success) {
        val msg = when (deleteMode) {
            DeleteMode.DELETE_ONE -> "선택한 손바닥 정보가 삭제되었습니다."
            DeleteMode.DELETE_ALL -> "등록된 손바닥 정보가 모두 삭제되었습니다."
            else -> "삭제되었습니다."
        }

        ResultScreen(
            message = msg,
            buttonText = "메인으로 돌아가기",
            onButtonClick = {
                viewModel.clearActionState()
                deleteMode = DeleteMode.NONE
                selectedPalm = null
                onGoMain()
            }
        )
        return
    }

    // 개별 삭제 확인 화면
    if (deleteMode == DeleteMode.DELETE_ONE && selectedPalm != null) {
        ConfirmYesNoScreen(
            message = "해당 손바닥 인증정보를\n삭제하시겠습니까?",
            uiState = actionState,
            onYesClick = { viewModel.deletePalm(selectedPalm!!.id) },
            onNoClick = {
                viewModel.clearActionState()
                deleteMode = DeleteMode.NONE
                selectedPalm = null
                // Confirm에서 “아니오” 눌렀을 때는 목록으로 돌아가면 되므로 onCancel 호출 X
            },
            errorMessage = "손바닥 정보 삭제 중 오류가 발생했습니다."
        )
        return
    }

    // 전체 삭제 확인 화면
    if (deleteMode == DeleteMode.DELETE_ALL) {
        ConfirmYesNoScreen(
            message = "등록된 손바닥 인증정보를\n모두 삭제하시겠습니까?",
            uiState = actionState,
            onYesClick = { viewModel.deleteAllPalms() },
            onNoClick = {
                viewModel.clearActionState()
                deleteMode = DeleteMode.NONE
                // 목록으로 돌아감
            },
            errorMessage = "전체 삭제 중 오류가 발생했습니다."
        )
        return
    }

    // 기본 화면 = 목록 + “전체 삭제” 버튼(단일)
    DeletePalmprintListContentScrollable(
        palmsState = palmsState,
        onRowClickPalm = { palm ->
            selectedPalm = palm
            deleteMode = DeleteMode.DELETE_ONE
            viewModel.clearActionState()
        },
        onDeleteAllClick = {
            deleteMode = DeleteMode.DELETE_ALL
            viewModel.clearActionState()
        }
    )
}

@Composable
private fun DeletePalmprintListContentScrollable(
    palmsState: UiState<List<PalmInfo>>,
    onRowClickPalm: (PalmInfo) -> Unit,
    onDeleteAllClick: () -> Unit
) {
    val columns = listOf(
        TableColumn(title = "palm_id", width = 80),
        TableColumn(title = "등록일자", weight = 1f),
    )

    when (val ui = palmsState) {
        UiState.Idle -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("손바닥 정보를 준비 중...")
        }

        UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("오류 발생: ${ui.message}")
        }

        is UiState.Success -> {
            val palms = ui.data
            val rows = palms.map { palm ->
                listOf(
                    palm.id.toString(),
                    formatDateOnly(palm.createdAt)
                )
            }

            RootLayoutScrollable(
                sectionGap = 12.dp,
                header = { HeaderContainer() },
                body = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        TableView(
                            title = "등록된 손바닥 목록",
                            columns = columns,
                            rows = rows,
                            hasMoreData = false,
                            isLoading = false,
                            modifier = Modifier.fillMaxWidth(),
                            onRowClick = { index ->
                                val palm = palms.getOrNull(index) ?: return@TableView
                                onRowClickPalm(palm)
                            },
                            onLoadMore = {}, // 호출되지 않음
                            scrollEnabled = false
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                footer = {
                    Footer {
                        SingleCenterButton(
                            text = "전체 삭제",
                            onClick = onDeleteAllClick,
                            enabled = palms.isNotEmpty()
                        )
                    }
                }
            )
        }
    }
}

private fun formatDateOnly(iso: String): String {
    return iso.substringBefore("T").ifBlank { iso }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDeletePalmprintScreen_List() {
    val mockPalms = listOf(
        PalmInfo(
            id = 1,
            createdAt = "2026-02-09T00:58:22.904Z",
            userId = 10,
            updatedAt = "2026-02-09T00:58:22.904Z"
        ),
        PalmInfo(
            id = 2,
            createdAt = "2026-02-10T01:12:10.000Z",
            userId = 10,
            updatedAt = "2026-02-10T01:12:10.000Z"
        )
    )

    DeletePalmprintListContentScrollable(
        palmsState = UiState.Success(mockPalms),
        onRowClickPalm = {},
        onDeleteAllClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDeletePalmprintScreen_Empty() {
    DeletePalmprintListContentScrollable(
        palmsState = UiState.Success(emptyList()),
        onRowClickPalm = {},
        onDeleteAllClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDeletePalmprintScreen_Loading() {
    DeletePalmprintListContentScrollable(
        palmsState = UiState.Loading,
        onRowClickPalm = {},
        onDeleteAllClick = {}
    )
}
