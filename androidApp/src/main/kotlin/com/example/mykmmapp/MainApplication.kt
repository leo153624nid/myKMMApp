package com.example.mykmmapp

import android.app.Application
import com.example.mykmmapp.di.initKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(isDebug = BuildConfig.DEBUG)
    }
}