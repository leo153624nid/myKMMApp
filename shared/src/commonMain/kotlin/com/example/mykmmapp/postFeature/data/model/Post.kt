package com.example.mykmmapp.postFeature.data.model

import com.example.mykmmapp.database.PostEntity
import kotlinx.serialization.Serializable

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)

@Serializable
data class PostDto(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)

// PostDto → Post
fun PostDto.toDomain() = Post(
    id = id,
    userId = userId,
    title = title,
    body = body
)

// PostEntity → Post
fun PostEntity.toDomain() = Post(
    id = id.toInt(),
    userId = userId.toInt(),
    title = title,
    body = body
)

// Post → PostEntity
fun Post.toEntity() = PostEntity(
    id = id.toLong(),
    userId = userId.toLong(),
    title = title,
    body = body
)