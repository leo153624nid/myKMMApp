package com.example.mykmmapp.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class Database(driverFactory: DatabaseDriverFactory) {

    private val database = AppDatabase(driverFactory.createDriver())
    private val queries = database.postQueries

    fun selectAllPosts(): Flow<List<Post>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun insertPost(post: Post) {
        queries.insert(
            id = post.id.toLong(),
            userId = post.userId.toLong(),
            title = post.title,
            body = post.body,
        )
    }

    fun deleteAllPosts() {
        queries.deleteAll()
    }

}