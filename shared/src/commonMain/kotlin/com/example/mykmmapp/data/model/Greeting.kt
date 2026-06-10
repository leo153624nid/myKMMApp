package com.example.mykmmapp.data.model

import com.example.mykmmapp.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String = "Hello, ${platform.name}!"
}