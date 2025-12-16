package com.example.palmprint_recognition.ui.common.field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,

    modifier: Modifier = Modifier,

    fieldWidth: Dp? = null,

    readOnly: Boolean = false,
    enabled: Boolean = true,
    isPassword: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = label,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color(0xFF21272A)
        )

        TextField(
            value = value,
            onValueChange = { if (!readOnly) onValueChange(it) },

            modifier = Modifier
                .then(
                    if (fieldWidth == null)
                        Modifier.fillMaxWidth()     // ✅ [추가] null이면 최대 너비
                    else
                        Modifier.width(fieldWidth) // ✅ [추가] 개별 너비 사용
                )
                .heightIn(min = 56.dp)
                .background(Color(0xFFF2F4F8))
                .border(1.dp, Color(0xFFC1C7CD)),

            readOnly = readOnly,
            enabled = enabled,

            visualTransformation =
                if (isPassword) PasswordVisualTransformation()
                else VisualTransformation.None,

            keyboardOptions = KeyboardOptions(
                keyboardType =
                    if (isPassword) KeyboardType.Password
                    else KeyboardType.Text
            ),

            textStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = Color(0xFF697077)
            ),

            singleLine = true,

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF2F4F8),
                unfocusedContainerColor = Color(0xFFF2F4F8),
                disabledContainerColor = Color(0xFFF2F4F8),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}


// Preview
@Preview(showBackground = true)
@Composable
fun LabeledFieldPreview() {

    var name by remember { mutableStateOf("Alice") }
    var email by remember { mutableStateOf("alice@email.com") }
    var password by remember { mutableStateOf("12345") }

    Column(modifier = Modifier.padding(20.dp)) {

        //  이름 (읽기 전용)
        LabeledField(
            label = "이름",
            value = name,
            onValueChange = {},
            readOnly = true,
            enabled = false
        )

        //  이메일 (쓰기 가능)
        LabeledField(
            label = "이메일",
            value = email,
            onValueChange = { email = it }
        )

        //  비밀번호
        LabeledField(
            label = "비밀번호",
            value = password,
            onValueChange = { password = it },
            isPassword = true
        )
    }
}
