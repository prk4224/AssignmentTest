package com.jaehong.presentation.ui.screen.recent.header

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RecentHeaderItem(
    modifier: Modifier
) {
    Text(
        text = "최근 검색 이력",
        modifier = modifier.padding(top = 20.dp),
        textAlign = TextAlign.Center
    )
}