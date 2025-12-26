package com.example.palmprint_recognition.ui.user.features.histories.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.UserVerificationLog
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.PaginationUiState
import com.example.palmprint_recognition.ui.user.features.histories.viewmodel.HistoryListViewModel

@Composable
fun HistoryListScreen(
    onHistoryClick: (UserVerificationLog) -> Unit, // log 전체 전달
    viewModel: HistoryListViewModel = hiltViewModel()
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    HistoryListContent(
        listState = listState,
        onRowClick = { log -> onHistoryClick(log) },
        onLoadMore = viewModel::loadNextPage
    )
}

@Composable
private fun HistoryListContent(
    listState: PaginationUiState<UserVerificationLog>,
    onRowClick: (UserVerificationLog) -> Unit,
    onLoadMore: () -> Unit
) {
    RootLayoutWeighted(
        headerWeight = 2f,
        bodyWeight = 8f,
        footerWeight = 0f,

        header = { HeaderContainer() },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HistoryListTableSection(
                    listState = listState,
                    onRowClick = onRowClick,
                    onLoadMore = onLoadMore,
                    modifier = Modifier.fillMaxWidth()
                )

                listState.errorMessage?.let {
                    Text(text = it)
                }
            }
        },

        footer = { /* 없음 */ }
    )
}

/**
 * 히스토리 목록 테이블 (페이지네이션/무한스크롤 + row 클릭 가능)
 * 컬럼: 인증일시(createdAt), 위치(location), 성공여부(isSuccess -> 승인/거부)
 */
@Composable
private fun HistoryListTableSection(
    listState: PaginationUiState<UserVerificationLog>,
    onRowClick: (UserVerificationLog) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        TableColumn(title = "인증일시", weight = 1f),
        TableColumn(title = "위치", weight = 1f),
        TableColumn(title = "성공여부", width = 80)
    )

    val rows = listState.items.map { log ->
        listOf(
            log.createdAt,                 // 필요하면 포맷팅 함수로 보기 좋게 바꿔도 됨
            log.location,
            if (log.isSuccess) "승인" else "거부"
        )
    }

    TableView(
        title = "내 인증 내역",
        columns = columns,
        rows = rows,
        hasMoreData = listState.hasMore,
        isLoading = listState.isLoadingInitial || listState.isLoadingMore,
        modifier = modifier.fillMaxWidth(),
        onRowClick = { index ->
            val log = listState.items.getOrNull(index) ?: return@TableView
            onRowClick(log) // log 전체 전달
        },
        onLoadMore = onLoadMore,
        scrollEnabled = true
    )

}
