package com.jaehong.presentation.ui.screen.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jaehong.presentation.ui.screen.search.header.HeaderItem
import com.jaehong.presentation.ui.screen.search.info.SearchInfoItem
import com.jaehong.presentation.ui.screen.search.info.SearchInfoItems
import com.jaehong.presentation.ui.screen.search.progress.CircularProgressBar
import com.jaehong.presentation.ui.screen.search.snack.SnackbarScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val searchList = searchViewModel.searchList.collectAsState().value
    val searchKeyword = searchViewModel.searchKeyword.collectAsState().value
    val snackbarState = searchViewModel.snackbarState.collectAsState().value
    val progressBarState = searchViewModel.uiState.collectAsState().value

    val (text, setText) = rememberSaveable { mutableStateOf(searchKeyword) }
    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
                searchOnClicked = {
                    if(text == "") searchViewModel.clearSearchList()
                    else {
                        searchViewModel.showProgressBar()
                        searchViewModel.getSearchList(text)
                    }

                },
                recentOnClicked = { searchViewModel.onNavigateToRecentClicked() }
            )
        },
        itemScreen = {
            SearchInfoItem(
                item = it,
                itemOnClicked = { searchViewModel.onNavigateToWebViewClicked(it.link) }
            )
        }
    )

    if (snackbarState) {
        SnackbarScreen(snackBarState = snackBarState,
            coroutineScope = coroutineScope,
            hideSnackbar = { searchViewModel.hideSnackBar() }
        )
    }

    if(progressBarState) {
        CircularProgressBar()
    }
}