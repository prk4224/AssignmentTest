package com.jaehong.presentation.ui.screen.recent.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.jaehong.domain.model.RecentInfo

@Composable
fun RecentInfoItem(
    modifier: Modifier,
    recentItem: RecentInfo,
    itemClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { itemClicked() }
    ) {
        Text(
            text = recentItem.keyword,
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }

}