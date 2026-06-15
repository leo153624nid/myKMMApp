package com.example.mykmmapp.postFeature.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mykmmapp.navigation.AppNavigator
import com.example.mykmmapp.postFeature.data.model.Post
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    vm: PostsViewModel = koinViewModel(),
    navigator: AppNavigator = koinInject(),
) {
    val state by vm.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        vm.effect.collect { effect ->
            when (effect) {
                is PostsEffect.NavigateToDetail -> { navigator.toPostDetail(effect.postId) }
                is PostsEffect.ShowError -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = "Retry",
                        duration = SnackbarDuration.Long,
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> vm.handleIntent(PostsIntent.LoadNextPage)
                        SnackbarResult.Dismissed -> {  } // nothing to do
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Posts") },
                navigationIcon = {
                    IconButton(onClick = { navigator.back() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { vm.handleIntent(PostsIntent.OpenFilterSheet) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                            contentDescription = null,
                            tint = if (state.selectedUserId != null)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ,
        ) {
            PostsContent(
                state,
                onIntent = vm::handleIntent
            )
        }
    }

    // Bottom Sheet
    if (state.isFilterSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { vm.handleIntent(PostsIntent.CloseFilterSheet) },
            sheetState = sheetState,
        ) {
            FilterSheetContent(
                selectedUserId = state.selectedUserId,
                onApply = { userId -> vm.handleIntent(PostsIntent.ApllyFilter(userId)) },
                onClear = { vm.handleIntent(PostsIntent.ApllyFilter(null)) },
            )
        }
    }
}

@Composable
fun PostsContent(
    state: PostsUiState,
    onIntent: (PostsIntent) -> Unit
) {
    val listState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisible != null && lastVisible.index >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onIntent(PostsIntent.LoadNextPage)
        }
    }

    Box(
       modifier = Modifier.fillMaxSize(),
    ) {
        when {
            // Loading
            state.isLoading -> {
                LoadingView()
            }

            // Error of first Load
            state.error != null && state.posts.isEmpty() -> {
                ErrorView(
                    message = state.error,
                    onClick = { onIntent(PostsIntent.Refresh) },
                )
            }

            else -> {
                Column {
                    // DB button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Offline Mode (show only DB posts)",
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                            ,
                        )

                        Checkbox(
                            checked = state.offlineMode,
                            onCheckedChange = {
                                onIntent(PostsIntent.OfflineModeClicked(it))
                            },
                        )
                    }

                    // List
                    PostList(
                        dataState = state,
                        listState = listState,
                        onClick = onIntent,
                    )
                }

                // Error next page loading
                if (state.error != null && state.posts.isNotEmpty()) {
                    Snackbar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        action = {
                            TextButton(onClick = { onIntent(PostsIntent.LoadNextPage) }) {
                                Text("Retry")
                            }
                        }
                    ) {
                        Text(state.error)
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun ErrorView(
    message: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
            )

            Button(onClick = onClick) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun PostList(
    dataState: PostsUiState,
    listState: LazyListState,
    onClick: (PostsIntent) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = dataState.posts,
            key = { it.id },
        ) { post ->
            PostCell(
                post = post,
                onClick = { onClick(PostsIntent.PostClicked(post)) },
            )
        }

        item {
            when {
                dataState.isLoadingMore -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                        ,
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    }
                }

                !dataState.canLoadMore -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Все посты загружены",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PostCell(
    post: Post,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = post.body,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
            )
        }
    }
}

@Composable
fun FilterSheetContent(
    selectedUserId: Int?,
    onApply: (Int?) -> Unit,
    onClear: () -> Unit
) {
    // Локальный стейт шита — пока пользователь не нажал Apply
    // не отправляем Intent в ViewModel
    var localSelected by remember(selectedUserId) {
        mutableStateOf(selectedUserId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),  // отступ от нижнего края
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Drag handle — полоска сверху (опционально, Material3 добавляет сам)
        Text(
            text = "Фильтры",
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalDivider()

        Text(
            text = "По пользователю",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Чипы с userId 1–5
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..5).forEach { userId ->
                FilterChip(
                    selected = localSelected == userId,
                    onClick = {
                        localSelected = if (localSelected == userId) null else userId
                    },
                    label = { Text("User $userId") }
                )
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Сброс фильтра
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onClear,
                enabled = selectedUserId != null
            ) {
                Text("Сбросить")
            }

            // Применить
            Button(
                modifier = Modifier.weight(1f),
                onClick = { onApply(localSelected) }
            ) {
                Text("Применить")
            }
        }
    }
}