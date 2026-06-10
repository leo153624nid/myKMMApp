package com.example.mykmmapp.postFeature.data.repository

import com.example.mykmmapp.postFeature.data.model.Post
import com.example.mykmmapp.postFeature.data.network.PostApi

interface PostRepository {
    suspend fun getPosts(page: Int): Result<List<Post>>
    suspend fun getPost(id: Int): Result<Post>
}

class PostRepositoryImpl(
    private val api: PostApi
): PostRepository {

    override suspend fun getPosts(page: Int): Result<List<Post>> {
        return runCatching { api.getPosts(page) }
    }

    override suspend fun getPost(id: Int): Result<Post> {
        return runCatching { api.getPost(id) }
    }

}