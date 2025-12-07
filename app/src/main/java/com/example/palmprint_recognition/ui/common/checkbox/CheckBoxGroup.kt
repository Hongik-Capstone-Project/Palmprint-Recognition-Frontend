package com.example.palmprint_recognition.ui.common.checkbox

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * ✅ 체크박스 그룹 (라디오 역할)
 *
 * @param options 체크 항목 리스트
 * @param selectedIndex 현재 선택된 인덱스
 * @param readOnly 읽기 전용 여부
 * @param onSelect 선택 변경 콜백
 */
@Composable
fun CheckBoxGroup(
    options: List<String>,
    selectedIndex: Int,                  // ✅ [핵심] 현재 선택값
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    onSelect: (Int) -> Unit              // ✅ [핵심] 선택 결과 상위 전달
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        options.forEachIndexed { index, text ->

            CheckBox(
                checked = index == selectedIndex,
                text = text,
                readOnly = readOnly,
                onCheckedChange = {
                    if (!readOnly) {
                        onSelect(index)  // ✅ [핵심] 하나만 선택되게 강제
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCheckBoxGroup() {
    CheckBoxGroup(
        options = listOf("처리 전", "승인", "기각"),
        selectedIndex = 0,  // ✅ default = "처리 전"
        readOnly = false,
        onSelect = {}
    )
}
