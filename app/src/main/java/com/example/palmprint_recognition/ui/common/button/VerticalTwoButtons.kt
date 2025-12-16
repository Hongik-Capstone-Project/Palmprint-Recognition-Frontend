package com.example.palmprint_recognition.ui.common.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 세로 버튼 2개 그룹
 * - 버튼 디자인을 외부에서 선택적으로 지정할 수 있도록 nullable 파라미터 제공
 */
@Composable
fun VerticalTwoButtons(
    firstText: String,
    secondText: String,
    onFirstClick: () -> Unit,
    onSecondClick: () -> Unit,

    // optional style params
    width: Dp? = null,
    height: Dp? = null,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    textColor: Color? = null,
    textSize: Int? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PrimaryButton(
            text = firstText,
            onClick = onFirstClick,
            modifier = Modifier.fillMaxWidth(),
            width = width ?: 356.dp,
            height = height ?: 56.dp,
            backgroundColor = backgroundColor ?: Color(0xFFC1C7CD),
            borderColor = borderColor ?: Color(0xFFC1C7CD),
            textColor = textColor ?: Color.White,
            textSize = textSize ?: 20
        )

        PrimaryButton(
            text = secondText,
            onClick = onSecondClick,
            modifier = Modifier.fillMaxWidth(),
            width = width ?: 356.dp,
            height = height ?: 56.dp,
            backgroundColor = backgroundColor ?: Color(0xFFC1C7CD),
            borderColor = borderColor ?: Color(0xFFC1C7CD),
            textColor = textColor ?: Color.White,
            textSize = textSize ?: 20
        )
    }
}
