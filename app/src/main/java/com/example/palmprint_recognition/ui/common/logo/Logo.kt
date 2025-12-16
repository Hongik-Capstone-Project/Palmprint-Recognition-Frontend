package com.example.palmprint_recognition.ui.common.logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.R

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    width: Dp = 186.dp
) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Palm AI Logo",
        modifier = modifier.width(width),
        contentScale = ContentScale.Fit
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLogo() {
    Logo(width = 140.dp)
}
