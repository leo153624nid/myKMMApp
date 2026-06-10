package com.example.mykmmapp.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface AppNavigator {
    val events: SharedFlow<Destination>

    fun toMain()
    fun back()
    fun toPosts()
    fun toPostDetail(postId: Int)
}

class RootNavigator: AppNavigator {

    private val _events = MutableSharedFlow<Destination>(extraBufferCapacity = 1)
    override val events = _events.asSharedFlow()

    override fun toMain() {
        _events.tryEmit(Destination.MainScreen)
    }

    override fun back() {
        _events.tryEmit(Destination.Back)
    }

    override fun toPosts() {
        _events.tryEmit(Destination.PostsScreen)
    }

    override fun toPostDetail(postId: Int) {
        _events.tryEmit(Destination.PostDetailScreen(postId))
    }

}