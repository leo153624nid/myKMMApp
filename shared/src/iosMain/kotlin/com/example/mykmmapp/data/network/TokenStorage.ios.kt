package com.example.mykmmapp.data.network

import platform.Foundation.NSUserDefaults

actual class TokenStorageImp: TokenStorage { // TODO: NSUserDefaults for pet, Keychain for Prod

    actual override suspend fun getToken(): String? {
        return NSUserDefaults.standardUserDefaults.stringForKey("token")
    }

    actual override suspend fun saveToken(token: String) {
        NSUserDefaults.standardUserDefaults.setObject(token, "token")
    }

    actual override suspend fun clearToken() {
        NSUserDefaults.standardUserDefaults.removeObjectForKey("token")
    }

}