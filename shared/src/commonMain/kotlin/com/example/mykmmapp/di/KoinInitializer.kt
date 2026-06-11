package com.example.mykmmapp.di

import com.example.mykmmapp.Platform
import com.example.mykmmapp.getPlatform
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(isDebug: Boolean) {
    val platformModule = module {
        single<Platform> { getPlatform(isDebug) }
    }

    startKoin {
        modules(
            platformModule + appModules
        )
    }
}