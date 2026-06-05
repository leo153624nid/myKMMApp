package com.example.mykmmapp.navigation

sealed class Destination(val route: String) {

    data object First: Destination(ROUTE_FIRST)
    data object Second: Destination(ROUTE_SECOND)

    companion object {
        private const val ROUTE_FIRST = "route_first"
        private const val ROUTE_SECOND = "route_second"
    }
}