package com.example.mykmmapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mykmmapp.App
import com.example.mykmmapp.ListContent
import com.example.mykmmapp.ListScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    navigator: AppNavigator
) {
    // подписываемся на события навигации
    LaunchedEffect(Unit) {
        navigator.events.collect { event ->
            when (event) {
                Destination.FirstScreen -> navHostController.navigate(Destination.FirstScreen)
                Destination.SecondScreen -> navHostController.navigate(Destination.SecondScreen)
                is Destination.DetailScreen -> navHostController.navigate(Destination.DetailScreen(event.id))
                Destination.Back -> navHostController.popBackStack()
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = Destination.FirstScreen
    ) {
        composable<Destination.FirstScreen> {
            App(
                navigator = navigator
            )
        }

        composable<Destination.SecondScreen> {
            ListScreen(
                navigator = navigator
            )
        }

        composable<Destination.DetailScreen> {
            val dest: Destination.DetailScreen = it.toRoute()
            ListScreen(
                navigator = navigator,
                id = dest.id
            )
        }
    }
}

class AppNavigator { // TODO: interface

    private val _events = MutableSharedFlow<Destination>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun toSecond() {
        _events.tryEmit(Destination.SecondScreen)
    }

    fun toDetail(id: String) {
        _events.tryEmit(Destination.DetailScreen(id))
    }

    fun back() {
        _events.tryEmit(Destination.Back)
    }
}