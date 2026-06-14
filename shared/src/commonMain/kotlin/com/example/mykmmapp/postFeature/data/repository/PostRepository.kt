package com.example.mykmmapp.postFeature.data.repository

import com.example.mykmmapp.database.Database
import com.example.mykmmapp.postFeature.data.model.Post
import com.example.mykmmapp.postFeature.data.model.toDomain
import com.example.mykmmapp.postFeature.data.model.toEntity
import com.example.mykmmapp.postFeature.data.network.PostApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PostRepository {
    // Network
    val pageSize: Int
    suspend fun getPosts(page: Int): Result<List<Post>>
    suspend fun getPost(id: Int): Result<Post>

    // Database
    fun getCachedPosts(): Flow<List<Post>>
    fun cachePosts(posts: List<Post>)
    fun clearCache()
}

class PostRepositoryImpl(
    private val api: PostApi,
    private val db: Database,
): PostRepository {

    // MARK: - Network
    override val pageSize: Int = api.PAGE_SIZE

    override suspend fun getPosts(page: Int): Result<List<Post>> {
        return runCatching {
            api.getPosts(page)
                .map { it.toDomain() }
        }
    }

    override suspend fun getPost(id: Int): Result<Post> {
        return runCatching {
            api.getPost(id)
                .toDomain()
        }
    }

    // MARK: - Database
    override fun getCachedPosts(): Flow<List<Post>> {
        return db.selectAllPosts()
            .map { list -> list.map { it.toDomain() } }
    }

    override fun cachePosts(posts: List<Post>) {
       posts.forEach {
           db.insertPost(it.toEntity())
       }
    }

    override fun clearCache() {
        db.deleteAllPosts()
    }

}