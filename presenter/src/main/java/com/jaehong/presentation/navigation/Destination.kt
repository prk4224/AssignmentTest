package com.jaehong.presentation.navigation

import com.jaehong.presentation.navigation.DestinationType.RECENT
import com.jaehong.presentation.navigation.DestinationType.SEARCH
import com.jaehong.presentation.navigation.DestinationType.WEB_VIE

sealed class Destination(protected val route: String, vararg params: String) {

    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    object Search : NoArgumentsDestination(SEARCH)
    object Recent : NoArgumentsDestination(RECENT)

    object WebView : Destination(WEB_VIE, "link") {
        const val LINK_KEY = "link"

        operator fun invoke(link: String): String = route.appendParams(
            LINK_KEY to link
        )
    }
}

private fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}