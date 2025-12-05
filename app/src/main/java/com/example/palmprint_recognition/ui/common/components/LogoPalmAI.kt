package com.example.palmprint_recognition.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.R

@Composable
fun LogoPalmAI(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.logo_palm_ai),
        contentDescription = "Palm AI Logo",
        modifier = modifier
            .width(186.dp)
            .height(79.dp)
    )
}

@Preview(showBackground = true, widthDp = 200, heightDp = 80)
@Composable
fun PreviewLogoPalmAI() {
    LogoPalmAI()
}
