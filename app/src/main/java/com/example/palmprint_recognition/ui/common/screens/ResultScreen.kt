package com.example.palmprint_recognition.ui.common.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.palmprint_recognition.ui.common.button.PrimaryButton
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.common.logo.Logo

@Composable
fun ResultScreen(
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RootLayoutWeighted(
        headerWeight = 3f,
        bodyWeight = 3f,
        footerWeight = 3f,

        // 상단은 비워서 공간만 확보
        header = {
            Spacer(modifier = Modifier.fillMaxSize())
        },

        body = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // CSS: logo 영역 (약 225px)
                Logo(width = 225.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // CSS: 14px / Regular / Black
                Text(
                    text = message,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(28.dp))

                PrimaryButton(
                    text = buttonText,
                    onClick = onButtonClick,
                    width = 289.dp,
                    height = 62.dp,
                    backgroundColor = Color(0xFFD9D9D9),
                    borderColor = Color(0xFFD9D9D9),
                    textColor = Color.Black,
                    textSize = 14
                )
            }
        },

        // 하단도 비워서 공간만 확보
        footer = {
            Spacer(modifier = Modifier.fillMaxSize())
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewResultScreen() {
    ResultScreen(
        message = "손바닥 등록을 완료했어요!",
        buttonText = "메인으로 돌아가기",
        onButtonClick = {}
    )
}
