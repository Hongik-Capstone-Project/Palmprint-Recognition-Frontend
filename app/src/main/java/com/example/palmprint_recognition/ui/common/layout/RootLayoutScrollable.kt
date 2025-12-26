package com.example.palmprint_recognition.ui.common.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RootLayoutScrollable(
    modifier: Modifier = Modifier,
    sectionGap: Dp = 12.dp,
    header: (@Composable () -> Unit)? = null,
    body: @Composable () -> Unit,
    footer: (@Composable () -> Unit)? = null
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {

        if (header != null) {
            item {
                header()
                Spacer(modifier = Modifier.height(sectionGap))
            }
        }

        item { body() }

        if (footer != null) {
            item {
                Spacer(modifier = Modifier.height(sectionGap))
                footer()
                Spacer(modifier = Modifier.height(sectionGap))
            }
        }
    }
}
