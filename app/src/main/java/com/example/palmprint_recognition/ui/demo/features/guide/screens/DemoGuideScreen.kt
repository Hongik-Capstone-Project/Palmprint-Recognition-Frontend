package com.example.palmprint_recognition.ui.demo.features.guide.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig

@Composable
fun DemoGuideScreen() {
    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = {
            HeaderContainer()
        },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "데모 설정")

                Text(
                    text = "손바닥 등록과 인증 화면에서 사용할 촬영 방식과 결과 표시 방식을 설정합니다.",
                    color = Color(0xFF697077)
                )

                Spacer(modifier = Modifier.height(4.dp))

                AutoCaptureSettingCard()

                SimilarityScoreSettingCard()
            }
        },
        footer = {
            Footer {
                // 설정 화면에서는 별도 버튼이 없어도 됩니다.
            }
        }
    )
}

@Composable
private fun AutoCaptureSettingCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF796E6E))
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "자동촬영")

                Text(
                    text = if (CameraAnalysisConfig.isAutoCaptureEnabled) {
                        "켜짐: 등록과 인증 모두 자동촬영으로 진행됩니다."
                    } else {
                        "꺼짐: 등록과 인증 모두 수동촬영으로 진행됩니다."
                    },
                    color = Color(0xFF697077)
                )
            }

            Switch(
                checked = CameraAnalysisConfig.isAutoCaptureEnabled,
                onCheckedChange = { checked ->
                    CameraAnalysisConfig.isAutoCaptureEnabled = checked
                }
            )
        }
    }
}

@Composable
private fun SimilarityScoreSettingCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF796E6E))
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "유사도 정보 출력")

                Text(
                    text = if (CameraAnalysisConfig.isSimilarityScoreVisible) {
                        "켜짐: 인증 결과 화면에 유사도 점수를 표시합니다."
                    } else {
                        "꺼짐: 인증 결과 화면에 유사도 점수를 표시하지 않습니다."
                    },
                    color = Color(0xFF697077)
                )
            }

            Switch(
                checked = CameraAnalysisConfig.isSimilarityScoreVisible,
                onCheckedChange = { checked ->
                    CameraAnalysisConfig.isSimilarityScoreVisible = checked
                }
            )
        }
    }
}