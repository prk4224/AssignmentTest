package com.jaehong.presentation.ui.screen.search

import android.accounts.NetworkErrorException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaehong.domain.model.ApiResult
import com.jaehong.domain.model.MovieItem
import com.jaehong.domain.usecase.GetSearchInfoUseCase
import com.jaehong.presentation.navigation.Destination
import com.jaehong.presentation.navigation.SearchAppNavigator
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
    private val searchAppNavigator: SearchAppNavigator
): ViewModel() {

    private val _searchList = MutableStateFlow<List<MovieItem>>(listOf())
    val searchList = _searchList.asStateFlow()


   fun getSearchList(keyword: String) {

       viewModelScope.launch {
           getSearchInfoUseCase(keyword).collectLatest{
               when(it){
                   is ApiResult.Error -> {
                       throw NetworkErrorException("Network Connect Error")
                   }
                   is ApiResult.Success -> {
                       _searchList.value = it.data.items
                   }
               }
           }
       }
   }

}