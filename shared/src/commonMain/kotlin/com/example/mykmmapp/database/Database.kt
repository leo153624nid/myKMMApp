package com.example.mykmmapp.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class Database(driverFactory: DatabaseDriverFactory) {

    private val database = AppDatabase(driverFactory.createDriver())
    private val queries = database.postQueries

    fun selectAllPosts(): Flow<List<PostEntity>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun insertPost(post: PostEntity) {
        queries.insert(
            id = post.id.toLong(),
            userId = post.userId.toLong(),
            title = post.title,
            desc = post.desc,
        )
    }

    fun insertPosts(posts: List<PostEntity>) {
        queries.transaction {
            posts.forEach {
                queries.insert(
                    id = it.id.toLong(),
                    userId = it.userId.toLong(),
                    title = it.title,
                    desc = it.desc,
                )
            }
        }
    }

    fun deleteAllPosts() {
        queries.deleteAll()
    }

}