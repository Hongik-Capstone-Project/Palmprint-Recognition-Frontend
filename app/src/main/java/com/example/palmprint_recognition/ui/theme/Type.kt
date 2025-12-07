package com.example.palmprint_recognition.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    // TITLE (예: "유저 목록")
    titleMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 15.sp,
        color = Color(0xFF21272A)
    ),

    // UserCard 이름 (Subtitle/M)
    titleLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
        color = Color(0xFF21272A)
    ),

    // UserCard 이메일 (Body/M)
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp,
        color = Color(0xFF697077)
    ),

    // Table Header (Subtitle/S)
    labelLarge = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 15.sp,
        color = Color(0xFF121619)
    ),

    // Table Row (Body/S)
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        color = Color(0xFF121619)
    )
)
