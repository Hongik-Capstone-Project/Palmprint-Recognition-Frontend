package com.example.palmprint_recognition.ui.common.table

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun ActionCardListView(
    title: String? = null,
    items: List<ActionCardItem>,
    actionIcon: ImageVector,
    onActionClick: (ActionCardItem) -> Unit,
    modifier: Modifier = Modifier,

    // pagination/scroll
    scrollEnabled: Boolean = true,
    hasMoreData: Boolean = false,
    isLoading: Boolean = false,
    onLoadMore: (() -> Unit)? = null,

    // empty
    emptyText: String = "데이터 없음"
) {
    val listState = rememberLazyListState()

    // 무한스크롤은 필요할 때만
    InfiniteScrollHandlerCard(
        listState = listState,
        enabled = scrollEnabled && (onLoadMore != null) && hasMoreData && !isLoading
    ) {
        onLoadMore?.invoke()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (!title.isNullOrBlank()) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        if (scrollEnabled) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                if (items.isEmpty() && !isLoading) {
                    item { EmptyItem(emptyText) }
                }

                items(items, key = { it.key }) { item ->
                    ActionCardRow(
                        item = item,
                        actionIcon = actionIcon,
                        onActionClick = { onActionClick(item) }
                    )
                }

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (items.isEmpty() && !isLoading) {
                    EmptyItem(emptyText)
                } else {
                    items.forEach { item ->
                        ActionCardRow(
                            item = item,
                            actionIcon = actionIcon,
                            onActionClick = { onActionClick(item) }
                        )
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

/**
 * 카드형 Row
 * - 고정 높이 150dp
 * - 배경 #F5F6F6
 * - 테두리: left/top/bottom 1dp #697077 (right 없음)
 * - padding: 8dp 12dp
 * - 아이콘: 우상단 24dp, tint #21272A, 클릭 가능(아이콘만)
 * - 텍스트: 왼쪽 정렬 멀티라인
 */
@Composable
private fun ActionCardRow(
    item: ActionCardItem,
    actionIcon: ImageVector,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color(0xFFF5F6F6))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val borderColor = Color(0xFF697077)

                // left border
                drawLine(
                    color = borderColor,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
                // top border
                drawLine(
                    color = borderColor,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
                // bottom border
                drawLine(
                    color = borderColor,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // 우상단 아이콘 (아이콘만 클릭 가능)
        Icon(
            imageVector = actionIcon,
            contentDescription = "action",
            tint = Color(0xFF21272A),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .clickable { onActionClick() }
        )

        // 텍스트 영역 (멀티라인)
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(end = 36.dp), // 아이콘 영역 확보
            verticalArrangement = Arrangement.Center
        ) {
            item.lines.forEachIndexed { idx, line ->
                Text(
                    text = line,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF21272A)
                )
                if (idx != item.lines.lastIndex) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

/**
 * 리스트 아이템 모델
 */
data class ActionCardItem(
    val key: String,
    val lines: List<String>
)

/**
 * Empty 상태 표시
 */
@Composable
private fun EmptyItem(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color(0xFF21272A)
        )
    }
}

/**
 * 무한 스크롤 감지
 */
@Composable
private fun InfiniteScrollHandlerCard(
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
