package com.jaehong.presentation.ui.screen.search.info

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.jaehong.domain.model.MovieItem

@Composable
fun SearchInfoItems(
    list: LazyPagingItems<MovieItem>?,
    modifier: Modifier,
    headerItemScreen: @Composable () -> Unit,
    itemScreen: @Composable (MovieItem) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            headerItemScreen()
        }

        if(list != null) {
            items(list) { data ->
                itemScreen(data?:throw NullPointerException("Data Null Error"))
            }
        }
    }
}

