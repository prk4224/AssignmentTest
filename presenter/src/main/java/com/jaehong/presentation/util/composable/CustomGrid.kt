package com.jaehong.presentation.util.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehong.domain.model.RecentInfo

@Composable
fun CustomGrid(
    recentItems: List<RecentInfo>,
    deviceWidth: Int,
    recentItem: @Composable (Modifier, RecentInfo) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val rowList = getColumnSize(recentItems,deviceWidth)

        itemsIndexed(rowList) { idx, data ->
            Row {
                for(i in 0 until data) {
                    recentItem
                }
            }
        }
    }
}

private fun getColumnSize(
    recentItems: List<RecentInfo>,
    deviceWidth: Int,
): List<Int> {
    val temp = mutableListOf<Int>()
    var currentSize = 0

    recentItems.forEach {
        if(currentSize == 0) temp.add(0)
        currentSize += (it.keyword.length*30)
        if(currentSize >= deviceWidth) {
            currentSize -= (it.keyword.length*30)
            currentSize = 0
        } else {
            temp[temp.lastIndex]++
        }
    }
    return temp
}

