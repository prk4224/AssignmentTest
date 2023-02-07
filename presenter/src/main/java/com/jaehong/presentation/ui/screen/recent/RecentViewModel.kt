package com.jaehong.presentation.ui.screen.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaehong.domain.model.RecentInfo
import com.jaehong.domain.usecase.GetRecentInfoUseCase
import com.jaehong.presentation.navigation.Destination
import com.jaehong.presentation.navigation.SearchAppNavigator
import com.jaehong.presentation.util.checkedResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val searchAppNavigator: SearchAppNavigator,
    private val getRecentInfoUseCase: GetRecentInfoUseCase,
) : ViewModel() {

    private val _recentInfoList = MutableStateFlow(listOf<RecentInfo>())
    val recentInfoList = _recentInfoList.asStateFlow()

    init {
        getRecentInfoList()
    }

    private fun getRecentInfoList() {
        viewModelScope.launch {
            getRecentInfoUseCase().collectLatest {
                checkedResult(
                    dbResult = it,
                    success = { list ->
                        if(list.size > 10) {
                            _recentInfoList.value = list.subList(0,10)
                            val deleteList = list.subList(11,list.size)
                            deleteRecentInfoList(deleteList)
                        } else {
                            _recentInfoList.value = list
                        }
                    },
                    error = {  }
                )
            }
        }
    }

    fun onNavigateToSearchClicked(keyword: String) {
        viewModelScope.launch {
            searchAppNavigator.navigateTo(Destination.Search(keyword))
        }
    }

    fun deleteRecentInfo(recentInfo: RecentInfo) {
        deleteRecentInfoList(listOf(recentInfo))
    }

    private fun deleteRecentInfoList(recentList: List<RecentInfo>) {
        viewModelScope.launch {
            getRecentInfoUseCase.deleteRecentInfoList(recentList)
        }
    }
}