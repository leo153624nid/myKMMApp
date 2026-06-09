package com.example.mykmmapp.postFeature.data.network

import com.example.mykmmapp.Platform
import com.example.mykmmapp.getPlatform
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(Logging) {
        val isDebug = getPlatform().isDebug
        level = if (isDebug) LogLevel.BODY else LogLevel.NONE
    }
}