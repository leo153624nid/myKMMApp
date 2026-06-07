package com.example.mykmmapp.navigation

import kotlinx.serialization.Serializable

//sealed class Destination(val route: String) {
//
//    data object First: Destination(ROUTE_FIRST)
//    data object Second: Destination(ROUTE_SECOND)
//
//    companion object {
//        private const val ROUTE_FIRST = "route_first"
//        private const val ROUTE_SECOND = "route_second"
//    }
//}

sealed interface Destination {

    @Serializable
    data object FirstScreen : Destination

    @Serializable
    data object SecondScreen : Destination

    @Serializable
    data class DetailScreen(val id: String) : Destination

    @Serializable
    data object Back : Destination
}