package com.example.mykmmapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.example.mykmmapp.navigation.AppNavHost
import com.example.mykmmapp.navigation.AppNavigator

fun MainViewController() = ComposeUIViewController {
    val navController = rememberNavController()
    val navigator = remember { AppNavigator() }

    AppNavHost(
        navHostController = navController,
        navigator = navigator
    )
}