package com.jaehong.presentation.ui.screen.recent.info

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehong.domain.model.RecentInfo

@Composable
fun RecentInfoItems(
    recentItems: List<RecentInfo>,
    headerItem: @Composable (Modifier) -> Unit,
    recentItem: @Composable (Modifier,RecentInfo) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        headerItem(
            Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(40.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            content = {
                items(recentItems) {
                    recentItem(
                        Modifier
                            .height(70.dp)
                            .padding(10.dp)
                            .border(3.dp, Color.Gray,RoundedCornerShape(20)),
                        it)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
        )
    }
}
