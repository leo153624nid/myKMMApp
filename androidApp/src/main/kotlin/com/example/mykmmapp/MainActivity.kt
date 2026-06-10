package com.example.mykmmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.mykmmapp.navigation.AppNavHost
import com.example.mykmmapp.navigation.AppNavigator
import com.example.mykmmapp.navigation.RootNavigator

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val navigator: AppNavigator = remember { RootNavigator() }

            AppNavHost(
                navHostController = navController,
                navigator = navigator
            )
        }
    }
}