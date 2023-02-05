package com.jaehong.presentation.ui.screen.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaehong.domain.usecase.GetRecentInfoUseCase
import com.jaehong.presentation.navigation.Destination
import com.jaehong.presentation.navigation.SearchAppNavigator
import com.jaehong.presentation.util.checkedResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val searchAppNavigator: SearchAppNavigator,
    private val getRecentInfoUseCase: GetRecentInfoUseCase,
) : ViewModel() {

    private val _recentInfoList = MutableStateFlow(listOf<String>())
    val recentInfoList = _recentInfoList.asStateFlow()

    init {
        viewModelScope.launch {
            getRecentInfoUseCase().collectLatest {
                checkedResult(
                    dbResult = it,
                    success = { list -> _recentInfoList.value = list}
                )
            }
        }
    }

    fun onNavigateToSearchClicked(keyword: String) {
        viewModelScope.launch {
            searchAppNavigator.navigateTo(Destination.Search(keyword))
        }
    }
}