package com.jaehong.presentation.ui.screen.wevview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jaehong.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _webViewLink = MutableStateFlow("")
    val webViewLink = _webViewLink.asStateFlow()

    init {
        val link = savedStateHandle.get<String>(Destination.WebView.LINK_KEY)
        _webViewLink.value = getDecode(link?: throw IllegalArgumentException("Link Error"))
    }

    private fun getDecode(link: String): String{
        return URLDecoder.decode(link,"UTF-8")
    }
}