package com.example.mykmmapp

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val isDebug: Boolean = true // TODO BuildConfig не доступен, почему? update for release
}

actual fun getPlatform(): Platform = AndroidPlatform()