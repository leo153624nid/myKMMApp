package com.example.mykmmapp

import android.os.Build

class AndroidPlatform(
    override val isDebug: Boolean
): Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(isDebug: Boolean): Platform = AndroidPlatform(isDebug)