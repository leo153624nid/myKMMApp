package com.example.mykmmapp.postFeature.data.network

import com.example.mykmmapp.postFeature.data.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PostApi(
    private val client: HttpClient
) {
    private val baseURL = "https://jsonplaceholder.typicode.com"
    val PAGE_SIZE = 15

    suspend fun getPosts(page: Int): List<Post> =
        client.get("$baseURL/posts") {
            parameter("_start", (page - 1) * PAGE_SIZE)
            parameter("_limit", PAGE_SIZE)
        }.body()

    suspend fun getPost(id: Int): Post = client.get("$baseURL/posts/$id").body()
}