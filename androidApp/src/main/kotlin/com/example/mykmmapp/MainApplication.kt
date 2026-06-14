package com.example.mykmmapp

import android.app.Application
import android.content.Context
import com.example.mykmmapp.di.initKoin
import org.koin.dsl.module

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            isDebug = BuildConfig.DEBUG,
            extraModules = listOf(
                module {
                    single<Context> { this@MainApplication }
                }
            )
        )
    }
}