package com.example.palmprint_recognition.ui.common.components.table

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * 테이블 전체 View
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableView(
    columns: List<TableColumn>,
    rows: List<List<String>>,
    hasMoreData: Boolean,     // 다음 페이지 존재 여부
    isLoading: Boolean,       // 페이지 로딩 여부
    modifier: Modifier = Modifier,
    onRowClick: (Int) -> Unit,
    onLoadMore: () -> Unit    // 무한스크롤 시 다음 페이지 요청
) {
    val listState = rememberLazyListState()

    // -----------------------------
    //  무한스크롤 감지
    // -----------------------------
    InfiniteScrollHandler(
        listState = listState,
        enabled = hasMoreData && !isLoading
    ) {
        onLoadMore()
    }

    Column(
        modifier = modifier.width(360.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.heightIn(max = 300.dp)
        ) {

            // sticky header (header fixed)
            stickyHeader {
                TableHeader(columns)
            }

            // 실제 데이터 표시
            itemsIndexed(rows) { index, list ->
                TableRow(
                    values = list,
                    columns = columns,
                    onClick = { onRowClick(index) }
                )
            }

            // 로딩 중(다음 페이지 로딩)
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Text("Loading more…")
                    }
                }
            }

            // 마지막 페이지
            if (!hasMoreData && rows.isNotEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Text("No more data")
                    }
                }
            }
        }
    }
}

/**
 * 무한 스크롤 감지
 */
@Composable
fun InfiniteScrollHandler(
    listState: LazyListState,
    enabled: Boolean,
    buffer: Int = 1,
    onLoadMore: () -> Unit
) {
    if (!enabled) return

    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisible >= totalItems - buffer
        }
            .distinctUntilChanged()
            .collectLatest { isBottom ->
                if (isBottom) onLoadMore()
            }
    }
}

@Preview(showBackground = true, heightDp = 350)
@Composable
fun PreviewTableView() {
    val columns = listOf(
        TableColumn("user_id", width = 60),
        TableColumn("email", weight = 1f)
    )

    val rows = listOf(
        listOf("1", "alice@example.com"),
        listOf("2", "bob@example.com"),
        listOf("3", "charlie@example.com")
    )

    TableView(
        columns = columns,
        rows = rows,
        hasMoreData = false,
        isLoading = false,
        onRowClick = {},
        onLoadMore = {}
    )
}
