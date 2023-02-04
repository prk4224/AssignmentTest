package com.jaehong.presentation.ui.screen.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jaehong.presentation.ui.screen.search.header.HeaderItem
import com.jaehong.presentation.ui.screen.search.info.SearchInfoItem
import com.jaehong.presentation.ui.screen.search.info.SearchInfoItems
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val searchList = searchViewModel.searchList.collectAsState().value

    val (text, setText) = rememberSaveable { mutableStateOf("") }

    SearchInfoItems(
        list = searchList,
        modifier = Modifier.fillMaxSize(),
        headerItemScreen = {
            HeaderItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(5.dp),
                text = text,
                setText = setText,
                searchOnClicked = { searchViewModel.getSearchList(text) },
                recentOnClicked = { }
            )
        },
        itemScreen = {
            SearchInfoItem(
                item = it,
                itemOnClicked = { searchViewModel.onNavigateToWebViewClicked(it.link) }
            )
        }
    )
}