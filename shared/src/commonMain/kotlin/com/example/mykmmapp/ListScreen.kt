package com.example.mykmmapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mykmmapp.navigation.AppNavigator

@Composable
fun ListScreen(
    navigator: AppNavigator,
    id: String? = null,
) {
    val id =  id ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(id)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.back()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ListContent()
            }
        },
    )
}

val elements = listOf("111", "222", "333", "444", "555")

@Composable
fun ListContent() {

    LazyColumn(
        modifier = Modifier
            .background(Color.Green)
            .fillMaxSize()
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(elements) { index, item ->
            Text(
                text = "$index " + item
            )
        }
    }
}