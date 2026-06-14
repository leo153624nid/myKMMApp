package com.example.mykmmapp.di

import com.example.mykmmapp.database.Database
import com.example.mykmmapp.database.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModule(): Module = module {
    single { DatabaseDriverFactory() }
    single { Database(get()) }
}