package com.jaehong.presentation.ui.screen.recent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.jaehong.presentation.ui.screen.recent.header.RecentHeaderItem
import com.jaehong.presentation.ui.screen.recent.info.RecentInfoItem
import com.jaehong.presentation.ui.screen.recent.info.RecentInfoItems
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun RecentScreen(
    recentViewModel: RecentViewModel = hiltViewModel()
) {
    val recentInfoList = recentViewModel.recentInfoList.collectAsState().value

    RecentInfoItems(
        recentItems = recentInfoList,
        recentItem = { modifier,recentInfo ->
            RecentInfoItem(
                modifier = modifier,
                recentItem = recentInfo,
                itemClicked = {
                    recentViewModel.onNavigateToSearchClicked(recentInfo.keyword)
                    recentViewModel.insertRecentInfo(recentInfo)
                }
            )
        },
        headerItem = { modifier -> RecentHeaderItem(modifier = modifier) }
    )
}