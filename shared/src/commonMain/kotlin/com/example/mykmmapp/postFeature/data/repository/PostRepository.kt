package com.example.mykmmapp.postFeature.data.repository

import com.example.mykmmapp.database.Database
import com.example.mykmmapp.postFeature.data.model.Post
import com.example.mykmmapp.postFeature.data.model.toDomain
import com.example.mykmmapp.postFeature.data.model.toEntity
import com.example.mykmmapp.postFeature.data.network.PostApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface PostRepository {
    // Network
    val pageSize: Int
    suspend fun getPosts(page: Int): Result<List<Post>>
    suspend fun getPost(id: Int): Result<Post>

    // Database
    suspend fun getCachedPosts(): Flow<List<Post>>
    suspend fun cachePosts(posts: List<Post>)
    suspend fun clearCache()
}

class PostRepositoryImpl(
    private val api: PostApi,
    private val db: Database,
): PostRepository {

    // MARK: - Network
    override val pageSize: Int = api.PAGE_SIZE

    override suspend fun getPosts(page: Int): Result<List<Post>> {
        return runCatching {
            val posts = api.getPosts(page)
            withContext(Dispatchers.Default) {
                posts.map { it.toDomain() }
            }
        }.onFailure { if (it is CancellationException) throw it }
    }

    override suspend fun getPost(id: Int): Result<Post> {
        return runCatching {
            val post = api.getPost(id)
            withContext(Dispatchers.Default) {
                post.toDomain()
            }
        }.onFailure { if (it is CancellationException) throw it }
    }

    // MARK: - Database
    override suspend fun getCachedPosts(): Flow<List<Post>> {
        return db.selectAllPosts()
            .flowOn(Dispatchers.IO)
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.Default)
    }

    override suspend fun cachePosts(posts: List<Post>) {
        val entities = withContext(Dispatchers.Default) {
            posts.map { it.toEntity() }
        }
        withContext(Dispatchers.IO) {
            db.insertPosts(entities)
        }
    }

    override suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            db.deleteAllPosts()
        }
    }

}