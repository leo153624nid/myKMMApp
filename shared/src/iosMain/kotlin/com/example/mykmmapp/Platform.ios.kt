package com.example.mykmmapp

import platform.UIKit.UIDevice

class IOSPlatform(
    override val isDebug: Boolean
): Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(isDebug: Boolean): Platform = IOSPlatform(isDebug)