package com.jaehong.presentation.ui.screen.search.info

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jaehong.domain.model.MovieItem

@Composable
fun SearchInfoItems(
    list: List<MovieItem>,
    modifier: Modifier,
    headerItemScreen: @Composable () -> Unit,
    itemScreen: @Composable (MovieItem) -> Unit,
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            headerItemScreen()
        }
        items(list) {
            itemScreen(it)
        }
    }
}

