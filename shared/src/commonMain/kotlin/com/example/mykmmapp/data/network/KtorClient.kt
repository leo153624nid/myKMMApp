package com.example.mykmmapp.data.network

import com.example.mykmmapp.Platform
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(
    platform: Platform,
    tokenStorage: TokenStorage
) = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(Auth) {
        bearer {
            loadTokens {
                val token = tokenStorage.getToken()
                if (token != null) {
                    BearerTokens(accessToken = token, refreshToken = "")
                } else null
            }

            refreshTokens {
                val newToken = refreshAccessToken()
                if (newToken != null) {
                    tokenStorage.saveToken(newToken)
                    BearerTokens(accessToken = newToken, refreshToken = "")
                } else null
            }
        }
    }
//    // плагин который добавляет заголовок к каждому запросу
//    install(createClientPlugin("AuthHeaderPlugin") {
//        onRequest { request, _ ->
//            val token = tokenStorage.getToken()
//            if (token != null) {
//                request.headers.append("Authorization", "Bearer $token")
//            }
//        }
//    })
    install(Logging) {
        level = if (platform.isDebug) LogLevel.BODY else LogLevel.NONE
    }
}

// Placeholder
private fun refreshAccessToken(): String? {
    return "mock_refreshToken"
}