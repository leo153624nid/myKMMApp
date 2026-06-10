package com.example.mykmmapp.di

import com.example.mykmmapp.postFeature.data.network.PostApi
import com.example.mykmmapp.data.network.createHttpClient
import com.example.mykmmapp.navigation.AppNavigator
import com.example.mykmmapp.navigation.RootNavigator
import com.example.mykmmapp.postFeature.data.repository.PostRepository
import com.example.mykmmapp.postFeature.data.repository.PostRepositoryImpl
import com.example.mykmmapp.postFeature.presentation.PostsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val navigatorModule = module {
    single<AppNavigator> { RootNavigator() }
}

val networkModule = module {
    single { createHttpClient() }
    single { PostApi(get()) }
}

val repositoryModule = module {
    single<PostRepository> { PostRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { PostsViewModel(get()) }
}

val appModules = listOf(
    navigatorModule,
    networkModule,
    repositoryModule,
    viewModelModule,
)