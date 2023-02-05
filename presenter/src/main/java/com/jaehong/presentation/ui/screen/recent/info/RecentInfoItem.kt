package com.jaehong.presentation.ui.screen.recent.info

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecentInfoItem(
    recentItem: String,
    itemClicked: () -> Unit
) {
    val width = remember { (35.dp) * recentItem.length }
    Box(
        modifier = Modifier
            .background(Color.White)
            .width(width)
            .border(2.dp, Color.DarkGray)
            .clip(RoundedCornerShape(20))
            .wrapContentWidth()
            .clickable { itemClicked() }
    ) {
        Text(
            text = recentItem,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }

}