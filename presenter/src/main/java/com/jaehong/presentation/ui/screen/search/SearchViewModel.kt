package com.jaehong.presentation.ui.screen.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jaehong.domain.model.MovieItem
import com.jaehong.domain.model.UiStateResult
import com.jaehong.domain.usecase.GetSearchInfoUseCase
import com.jaehong.presentation.navigation.Destination
import com.jaehong.presentation.navigation.SearchAppNavigator
import com.jaehong.presentation.ui.screen.search.paging.SearchInfoPageSource
import com.jaehong.presentation.util.checkedResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchInfoUseCase: GetSearchInfoUseCase,
    private val searchAppNavigator: SearchAppNavigator,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword = _searchKeyword.asStateFlow()

    private val _snackbarState = MutableStateFlow(false)
    val snackbarState = _snackbarState.asLiveData()

    private val _uiState = MutableStateFlow(UiStateResult.SUCCESS)
    val uiState  = _uiState.asLiveData()

    var searchList: Flow<PagingData<MovieItem>>? = null

    init {
        val keyword = savedStateHandle.get<String>(Destination.Search.KEYWORD_KEY)
        if (keyword != null) {
            _searchKeyword.value = keyword
            setSearchList(keyword)
        }
    }

    fun setSearchList(keyword: String) {
        searchList = Pager(
            PagingConfig(pageSize = 10)
        ) { SearchInfoPageSource(
            keyword = keyword,
            getSearchInfoUseCase = getSearchInfoUseCase,
            hideProgressBar = { hideProgressBar() },
            checkSearchListSize = { keyword, size ->
                checkSearchListSize(size,keyword)
            },
            setError = { _uiState.value = UiStateResult.ERROR }
        ) }.flow.cachedIn(viewModelScope)
    }


    // Test
    fun getSearchList(keyword: String) {
        viewModelScope.launch {
            checkedResult(
                apiResult = getSearchInfoUseCase(keyword,1),
                success = {
                    hideProgressBar()
                    checkSearchListSize(it.items.size,keyword)
                },
                error = {
                    _uiState.value = UiStateResult.ERROR
                }
            )
        }
    }

    fun showProgressBar() {
        _uiState.value = UiStateResult.LOADING
    }

    private fun hideProgressBar() {
        _uiState.value = UiStateResult.SUCCESS
    }

    private fun showSnackBar() {
        _snackbarState.value = true
    }

    fun hideSnackBar() {
        _snackbarState.value = false
    }

    fun onNavigateToWebViewClicked(link: String) {
        viewModelScope.launch {
            val encodedUrl =
                withContext(Dispatchers.IO) {
                    URLEncoder.encode(link, "UTF-8")
                }
            searchAppNavigator.navigateTo(Destination.WebView(encodedUrl))
        }
    }

    fun onNavigateToRecentClicked() {
        viewModelScope.launch {
            searchAppNavigator.navigateTo(Destination.Recent())
        }
    }

    private fun insertRecentInfo(keyword: String) {
        viewModelScope.launch {
            getSearchInfoUseCase.insertRecentInfo(keyword)
        }
    }

    private fun checkSearchListSize(size: Int,keyword: String) {
        if(size > 0) {
            insertRecentInfo(keyword)
        } else {
            showSnackBar()
        }
    }
}