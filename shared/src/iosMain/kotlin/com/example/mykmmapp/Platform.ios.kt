package com.example.mykmmapp

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val isDebug: Boolean = true // TODO: update for release
}

actual fun getPlatform(): Platform = IOSPlatform()