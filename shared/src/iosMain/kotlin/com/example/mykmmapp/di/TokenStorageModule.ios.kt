package com.example.mykmmapp.di

import com.example.mykmmapp.data.network.TokenStorage
import com.example.mykmmapp.data.network.TokenStorageImp
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun tokenStorageModule(): Module = module {
    single<TokenStorage> { TokenStorageImp() }
}