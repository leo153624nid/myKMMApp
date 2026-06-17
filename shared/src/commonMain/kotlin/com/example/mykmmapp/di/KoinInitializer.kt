package com.example.mykmmapp.di

import com.example.mykmmapp.Platform
import com.example.mykmmapp.getPlatform
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(
    isDebug: Boolean,
    extraModules: List<Module> = emptyList()
) {
    val platformModule = module {
        single<Platform> { getPlatform(isDebug) }
    }
    val databaseModule = databaseModule()
    val tokenStorageModule = tokenStorageModule()

    startKoin {
        modules(
            extraModules +
            platformModule +
            databaseModule +
            tokenStorageModule +
            appModules
        )
    }
}