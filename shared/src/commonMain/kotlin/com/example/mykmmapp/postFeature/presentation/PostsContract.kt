package com.example.mykmmapp.postFeature.presentation

import com.example.mykmmapp.postFeature.data.model.Post

data class PostsUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed class PostsIntent {
    object LoadPosts: PostsIntent()
    object  Refresh: PostsIntent()
    data class PostClicked(val post: Post): PostsIntent()
}

sealed class PostsEffect {
    data class NavigateToDetail(val postId: Int): PostsEffect()
    data class ShowError(val message: String): PostsEffect()
}