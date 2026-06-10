package com.example.mykmmapp.postFeature.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mykmmapp.navigation.AppNavigator
import com.example.mykmmapp.postFeature.data.model.Post
import com.example.mykmmapp.postFeature.data.repository.PostRepository
import org.koin.compose.koinInject
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn

@Composable
fun PostDetailScreen(
    navigator: AppNavigator = koinInject(),
    postId: Int,
    repository: PostRepository = koinInject() // простой inject без ViewModel
) {
    var post by remember { mutableStateOf<Post?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(postId) {
        isLoading = true

        repository.getPost(postId)
            .onSuccess {
                post = it
                isLoading = false
            }
            .onFailure {
                error = it.message
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post #$postId") },
                navigationIcon = {
                    IconButton(onClick = { navigator.back() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                            contentDescription = "Back",
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
            when {
                isLoading -> LoadingView()

                error != null -> ErrorView(message = error ?: "", onClick = {  })

                post != null -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = post?.title ?: "",
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    HorizontalDivider()

                    Text(
                        text = post?.body ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}