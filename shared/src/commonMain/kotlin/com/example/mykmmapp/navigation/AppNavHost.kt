package com.example.mykmmapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mykmmapp.ui.App
import com.example.mykmmapp.postFeature.presentation.PostDetailScreen
import com.example.mykmmapp.postFeature.presentation.PostsScreen
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
                Destination.MainScreen -> navHostController.navigate(Destination.MainScreen)
                Destination.PostsScreen -> navHostController.navigate(Destination.PostsScreen)
                is Destination.PostDetailScreen -> navHostController.navigate(Destination.PostDetailScreen(event.postId))
                Destination.Back -> navHostController.popBackStack()
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = Destination.MainScreen
    ) {
        composable<Destination.MainScreen> {
            App(
                navigator = navigator
            )
        }

        composable<Destination.PostsScreen> {
            PostsScreen(
                navigator = navigator,
            )
        }

        composable<Destination.PostDetailScreen> {
            val dest: Destination.PostDetailScreen = it.toRoute()

            PostDetailScreen(
                navigator = navigator,
                postId = dest.postId,
            )
        }
    }
}

class AppNavigator { // TODO: interface

    private val _events = MutableSharedFlow<Destination>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun toMain() {
        _events.tryEmit(Destination.MainScreen)
    }

    fun toPosts() {
        _events.tryEmit(Destination.PostsScreen)
    }

    fun toPostDetail(postId: Int) {
        _events.tryEmit(Destination.PostDetailScreen(postId))
    }

    fun back() {
        _events.tryEmit(Destination.Back)
    }
}