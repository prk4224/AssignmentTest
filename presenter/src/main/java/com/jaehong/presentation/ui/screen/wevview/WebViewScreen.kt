package com.jaehong.presentation.ui.screen.wevview

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun WebViewScreen(
    webViewViewModel: WebViewViewModel = hiltViewModel()
) {
   val link = webViewViewModel.webViewLink.collectAsState().value

    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(link)
        }
    }, update = {
        it.loadUrl(link)
    })

}