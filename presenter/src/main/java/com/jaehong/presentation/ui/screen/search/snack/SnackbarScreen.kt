package com.jaehong.presentation.ui.screen.search.snack

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jaehong.presentation.util.Constants.SEARCH_RESULT_NOTHING_MESSAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SnackbarScreen(
    snackBarState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    hideSnackbar: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        showSnackBarOneSecond(snackBarState, coroutineScope, hideSnackbar)
        SnackbarHost(
            hostState = snackBarState, modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        )
    }
}


private fun showSnackBarOneSecond(
    snackBarState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    hideSnackbar: () -> Unit
) {
    coroutineScope.launch {
        val scope = coroutineScope.launch {
            snackBarState.showSnackbar(
                message = SEARCH_RESULT_NOTHING_MESSAGE,
                duration = SnackbarDuration.Indefinite
            )
        }
        delay(1000L)
        hideSnackbar()
        scope.cancel()
    }
}