package com.example.mykmmapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mykmmapp.postFeature.presentation.PostDetailScreen
import com.example.mykmmapp.postFeature.presentation.PostsScreen
import com.example.mykmmapp.ui.MainScreen
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    navigator: AppNavigator = koinInject(),
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
            MainScreen()
        }

        composable<Destination.PostsScreen> {
            PostsScreen()
        }

        composable<Destination.PostDetailScreen> {
            val dest: Destination.PostDetailScreen = it.toRoute()

            PostDetailScreen(
                postId = dest.postId,
            )
        }
    }
}