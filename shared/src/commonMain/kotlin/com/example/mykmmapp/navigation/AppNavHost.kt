package com.example.mykmmapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mykmmapp.App
import com.example.mykmmapp.ListContent

@Composable
fun AppNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navHostController,
        startDestination = Destination.First
    ) {

        composable(route = Destination.First.route) {
            App()
        }

        composable(route = Destination.Second.route) {
            ListContent()
        }
    }
}