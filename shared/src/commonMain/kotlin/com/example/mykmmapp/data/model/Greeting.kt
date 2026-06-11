package com.example.mykmmapp.data.model

import com.example.mykmmapp.Platform
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Greeting: KoinComponent {
    private val platform: Platform by inject()

    fun greet(): String = "Hello, ${platform.name}!"
}