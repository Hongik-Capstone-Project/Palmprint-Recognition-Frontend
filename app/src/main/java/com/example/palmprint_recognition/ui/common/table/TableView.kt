package com.example.palmprint_recognition.ui.common.table

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
    title: String,
    columns: List<TableColumn>,
    rows: List<List<String>>,
    hasMoreData: Boolean = false,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    onRowClick: ((Int) -> Unit)? = null,
    onLoadMore: (() -> Unit)? = null
) {
    val listState = rememberLazyListState()

    // 무한스크롤은 onLoadMore 있을 때만
    InfiniteScrollHandler(
        listState = listState,
        enabled = (onLoadMore != null) && hasMoreData && !isLoading
    ) {
        onLoadMore?.invoke()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = title, modifier = Modifier.padding(12.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader { TableHeader(columns) }

            if (rows.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    ) {
                        Text(
                            text = "데이터 없음",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            itemsIndexed(rows) { index, list ->
                TableRow(
                    values = list,
                    columns = columns,
                    onClick = {
                        // 클릭 핸들러 있을 때만 동작
                        onRowClick?.invoke(index)
                    }
                )
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Text("Loading more…")
                    }
                }
            }

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
        listOf("3", "charlie@example.com"),
        listOf("4", "david@example.com"),
        listOf(
            "5",
            "verylongemmaaddress_test_case_01@example.com_verylongemmaaddress_test_case_02@example.com_verylongemmaaddress_test_case_03@example.com_verylongemmaaddress_test_case_04@example.com"
        ),
        listOf("6", "frank@example.com"),
        listOf("7", "grace@example.com"),
        listOf("8", "henry@example.com"),
        listOf("9", "irene@example.com"),
        listOf("10", "jack@example.com"),
        listOf("11", "kate@example.com"),
        listOf("12", "leo@example.com"),
        listOf("13", "mia@example.com"),
        listOf("14", "nick@example.com"),
        listOf("15", "olivia@example.com"),
        listOf("16", "paul@example.com"),
        listOf("17", "quinn@example.com"),
        listOf("18", "rose@example.com"),
        listOf("19", "sam@example.com"),
        listOf("20", "tina@example.com")
    )

    TableView(
        title = "유저 목록",
        columns = columns,
        rows = rows,
        hasMoreData = false,
        isLoading = false,
        onRowClick = {},
        onLoadMore = {}
    )
}
