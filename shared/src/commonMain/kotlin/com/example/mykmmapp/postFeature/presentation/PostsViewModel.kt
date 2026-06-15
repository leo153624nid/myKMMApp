package com.example.mykmmapp.postFeature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykmmapp.postFeature.data.network.PostApi
import com.example.mykmmapp.postFeature.data.repository.PostRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostsViewModel(
    private val repository: PostRepository,
): ViewModel() {

    private val _state = MutableStateFlow(PostsUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PostsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(PostsIntent.Refresh)
    }

    fun handleIntent(intent: PostsIntent) {
        when (intent) {
            is PostsIntent.Refresh -> refreshPosts()
            is PostsIntent.LoadNextPage -> loadNextPage()
            is PostsIntent.PostClicked -> navigateToDetail(intent.post.id)
            is PostsIntent.OfflineModeClicked -> handleOfllineModeClicked(intent.newValue)
        }
    }

    private fun refreshPosts() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    posts = emptyList(),
                    currentPage = 1,
                    canLoadMore = true,
                )
            }

            repository.getPosts(page = 1)
                .onSuccess { posts ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            posts = posts,
                            canLoadMore = posts.size >= repository.pageSize
                        )
                    }
                    repository.cachePosts(posts)
                }
                .onFailure { error ->
                    val message = error.message ?: "Unknown error"
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = message,
                        )
                    }
                    _effect.send(PostsEffect.ShowError(message))
                }
        }
    }

    fun loadNextPage() {
        val current = _state.value

        if (current.isLoading) return
        if (current.isLoadingMore) return
        if (!current.canLoadMore) return

        val nextPage = current.currentPage + 1

        viewModelScope.launch {
            _state.update {
                it.copy(isLoadingMore = true)
            }

            repository.getPosts(page = nextPage)
                .onSuccess { newPosts ->
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            posts = it.posts + newPosts,
                            currentPage = nextPage,
                            canLoadMore = newPosts.size >= repository.pageSize,
                        )
                    }
                    repository.cachePosts(newPosts)
                }
                .onFailure { error ->
                    val message = error.message ?: "Unknown error"
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            error = message,
                        )
                    }
                    _effect.send(PostsEffect.ShowError(message))
                }
        }
    }

    private fun navigateToDetail(postId: Int) {
        viewModelScope.launch {
            _effect.send(PostsEffect.NavigateToDetail(postId))
        }
    }

    private fun handleOfllineModeClicked(newValue: Boolean) {
        _state.update { it.copy(offlineMode = newValue) }
        if (newValue) {
            getCachedPosts()
        } else {
            refreshPosts()
        }
    }

    private fun getCachedPosts() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    posts = emptyList(),
                    currentPage = 1,
                    canLoadMore = false,
                )
            }

            repository.getCachedPosts().collect { posts ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        posts = posts,
                    )
                }
            }
        }
    }

}