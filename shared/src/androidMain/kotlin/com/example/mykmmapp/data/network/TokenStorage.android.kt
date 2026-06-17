package com.example.mykmmapp.data.network

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

actual class TokenStorageImp(private val context: Context): TokenStorage {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    actual override suspend fun getToken(): String? {
        return prefs.getString("token", null)
    }

    actual override suspend fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    actual override suspend fun clearToken() {
        prefs.edit().remove("token").apply()
    }

}