package com.example.mykmmapp

import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.example.mykmmapp.navigation.AppNavHost

fun MainViewController() = ComposeUIViewController {
    val navController = rememberNavController()

    AppNavHost(
        navHostController = navController,
    )
}