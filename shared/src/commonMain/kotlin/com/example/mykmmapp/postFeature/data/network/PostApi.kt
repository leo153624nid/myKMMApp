package com.example.mykmmapp.postFeature.data.network

import com.example.mykmmapp.postFeature.data.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PostApi(
    private val client: HttpClient
) {
    companion object {
        const val PAGE_SIZE = 15
        private const val baseURL = "https://jsonplaceholder.typicode.com"
    }

    suspend fun getPosts(page: Int, limit: Int = PAGE_SIZE): List<Post> =
        client.get("$baseURL/posts") {
            parameter("_start", (page - 1) * limit)
            parameter("_limit", limit)
        }.body()

    suspend fun getPost(id: Int): Post = client.get("$baseURL/posts/$id").body()
}