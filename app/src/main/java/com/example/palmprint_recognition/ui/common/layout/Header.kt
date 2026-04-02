package com.example.palmprint_recognition.ui.common.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.logo.Logo

@Composable
fun Header(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Logo(width = 180.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPalmHeader() {
    Header()
}