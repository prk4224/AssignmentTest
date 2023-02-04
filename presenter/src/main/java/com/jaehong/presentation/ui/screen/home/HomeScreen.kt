package com.jaehong.presentation.ui.screen.home

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jaehong.presentation.navigation.Destination
import com.jaehong.presentation.navigation.NavHost
import com.jaehong.presentation.navigation.NavigationIntent
import com.jaehong.presentation.navigation.composable
import com.jaehong.presentation.ui.screen.search.SearchScreen
import com.jaehong.presentation.ui.screen.wevview.WebViewScreen
import com.jaehong.presentation.ui.theme.AssignmentTestTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    NavigationEffects(
        navigationChannel = homeViewModel.navigationChannel,
        navHostController = navController
    )
    AssignmentTestTheme {
        NavHost(
            navController = navController,
            startDestination = Destination.Search) {

            composable(Destination.Search) {
                SearchScreen()
            }
            composable(Destination.WebView) {
                WebViewScreen()
            }

        }
    }
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }

            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }
                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}