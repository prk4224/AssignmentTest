package com.jaehong.presentation.ui.screen.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.jaehong.domain.model.MovieItem
import com.jaehong.domain.usecase.GetSearchInfoUseCase
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
class SearchViewModel @Inject constructor(
    private val getSearchInfoUseCase: GetSearchInfoUseCase,
    private val searchAppNavigator: SearchAppNavigator,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _searchList = MutableStateFlow<List<MovieItem>>(listOf())
    val searchList = _searchList.asStateFlow()

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword = _searchKeyword.asStateFlow()

    init {
        val keyword = savedStateHandle.get<String>(Destination.Search.KEYWORD_KEY)
        if (keyword != null) {
            _searchKeyword.value = keyword
            getSearchList(keyword)
        }
    }

    fun getSearchList(keyword: String) {
        viewModelScope.launch {
            getSearchInfoUseCase(keyword).collectLatest {
                checkedResult(
                    apiResult = it,
                    success = { data -> _searchList.value = data.items }
                )
            }
        }
    }

    fun clearSearchList() {
        _searchList.value = listOf()
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

    fun insertRecentInfo(keyword: String) {
        viewModelScope.launch {
            getSearchInfoUseCase.insertRecentInfo(keyword).collectLatest {
                checkedResult(
                    dbResult = it,
                    success = { massage -> Log.d("DB Insert Checked", massage) }
                )
            }
        }
    }
}