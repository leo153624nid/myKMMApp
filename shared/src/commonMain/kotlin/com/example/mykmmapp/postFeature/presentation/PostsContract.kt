package com.example.mykmmapp.postFeature.presentation

import com.example.mykmmapp.postFeature.data.model.Post

data class PostsUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val canLoadMore: Boolean = true,
    val currentPage: Int = 1,
    val offlineMode: Boolean = false,
)

sealed class PostsIntent {
    object Refresh: PostsIntent()
    object LoadNextPage: PostsIntent()
    data class PostClicked(val post: Post): PostsIntent()
    data class OfflineModeClicked(val newValue: Boolean): PostsIntent()
}

sealed class PostsEffect {
    data class NavigateToDetail(val postId: Int): PostsEffect()
    data class ShowError(val message: String): PostsEffect()
}