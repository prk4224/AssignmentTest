package com.jaehong.presentation.ui.screen.home

import androidx.lifecycle.ViewModel
import com.jaehong.presentation.navigation.SearchAppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    searchAppNavigator: SearchAppNavigator
) : ViewModel() {
    val navigationChannel = searchAppNavigator.navigationChannel
}