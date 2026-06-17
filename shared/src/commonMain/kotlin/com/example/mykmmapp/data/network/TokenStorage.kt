package com.example.mykmmapp.data.network

interface TokenStorage {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}

expect class TokenStorageImp: TokenStorage {
    override suspend fun getToken(): String?
    override suspend fun saveToken(token: String)
    override suspend fun clearToken()
}