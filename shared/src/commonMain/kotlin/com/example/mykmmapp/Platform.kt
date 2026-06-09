package com.example.mykmmapp

interface Platform {
    val name: String
    val isDebug: Boolean
}

expect fun getPlatform(): Platform