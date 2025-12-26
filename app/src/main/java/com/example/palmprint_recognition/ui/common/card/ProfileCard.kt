package com.example.palmprint_recognition.ui.common.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmprint_recognition.R

@Composable
fun ProfileCard(
    name: String,
    email: String,

    modifier: Modifier = Modifier,

    width: Dp = 368.dp,
    height: Dp = 48.dp
) {
    Row(
        modifier = modifier
            .width(width)
            .height(height),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(height)
                .background(Color(0xFFF2F4F8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_card),
                contentDescription = "Profile Icon",
                modifier = Modifier.size(height / 2)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF21272A)
            )
            Text(
                text = email,
                fontSize = 16.sp,
                color = Color(0xFF697077)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileCard() {
    ProfileCard(
        name = "Alice",
        email = "alice@example.com",
        modifier = Modifier.padding(20.dp)
    )
}
