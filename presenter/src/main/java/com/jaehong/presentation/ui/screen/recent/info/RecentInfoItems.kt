package com.jaehong.presentation.ui.screen.recent.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecentInfoItems(
    recentItems: List<String>,
    headerItem: @Composable (Modifier) -> Unit,
    recentItem: @Composable (String) -> Unit
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
            columns = GridCells.Adaptive(100.dp),
            content = {
                items(recentItems) {
                    recentItem(it)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
        )
    }

}