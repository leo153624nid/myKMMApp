package com.example.mykmmapp.postFeature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        handleIntent(PostsIntent.LoadPosts)
    }

    fun handleIntent(intent: PostsIntent) {
        when (intent) {
            is PostsIntent.LoadPosts -> loadPosts()
            is PostsIntent.Refresh -> loadPosts()
            is PostsIntent.PostClicked -> navigateToDetail(intent.post.id)
        }
    }

    private fun loadPosts() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, error = null)
            }

            repository.getPosts()
                .onSuccess { posts ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            posts = posts
                        )
                    }
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

    private fun navigateToDetail(postId: Int) {
        viewModelScope.launch {
            _effect.send(PostsEffect.NavigateToDetail(postId))
        }
    }

}