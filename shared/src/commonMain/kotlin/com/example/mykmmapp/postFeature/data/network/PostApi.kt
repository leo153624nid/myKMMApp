package com.example.mykmmapp.postFeature.data.network

import com.example.mykmmapp.postFeature.data.model.PostDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class PostApi(
    private val client: HttpClient
) {
    private val baseURL = "https://jsonplaceholder.typicode.com"
    val PAGE_SIZE = 15

    suspend fun getPosts(page: Int): List<PostDto> {
        return withContext(Dispatchers.IO) {
            client
                .get("$baseURL/posts") {
                    parameter("_start", (page - 1) * PAGE_SIZE)
                    parameter("_limit", PAGE_SIZE)
                }
                .body()
        }
    }

    suspend fun getPost(id: Int): PostDto {
        return withContext(Dispatchers.IO) {
            client
                .get("$baseURL/posts/$id")
                .body()
        }
    }
}