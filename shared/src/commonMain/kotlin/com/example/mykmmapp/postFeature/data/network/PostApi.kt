package com.example.mykmmapp.postFeature.data.network

import com.example.mykmmapp.postFeature.data.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PostApi(
    private val client: HttpClient
) {
    private val baseURL = "https://jsonplaceholder.typicode.com"

    suspend fun getPosts(): List<Post> = client.get("$baseURL/posts").body()

    suspend fun getPost(id: Int): Post = client.get("$baseURL/posts/$id").body()
}