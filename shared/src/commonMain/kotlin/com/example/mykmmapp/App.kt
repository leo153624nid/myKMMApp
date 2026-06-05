package com.example.mykmmapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.ui.draw.alpha

import mykmmapp.shared.generated.resources.Res
import mykmmapp.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    var userEmail by remember { mutableStateOf("") }
    var isEmailFormatValid by remember { mutableStateOf(true) }
    var validationMessage by remember { mutableStateOf("") }
    val testMail = "abc@abc.com"

    MaterialTheme {
        var showContent by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .background(Color.Red)
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(Color.Yellow)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
//                showContent = !showContent
//                val intent = Intent()
            }) {
                Text("Click me!")
            }

            ListScreen()

            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Image(
                        painter = painterResource(Res.drawable.compose_multiplatform),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(5.dp, CircleShape)
                            .clip(CircleShape)
                            .clickable(
                                onClick = {
                                    println("here")
                                },
                            )
                    )

                    Text(
                        text = "Compose: $greeting",
                        modifier = Modifier
                            .background(Color.Gray)
                            .padding(horizontal = 16.dp),
                        fontSize = 24.sp
                    )

                    MainCheckBox()

                    Spacer(Modifier.height(30.dp))

                    CheckEmailField(
                        email = userEmail,
                        isEmailValid = isEmailFormatValid,
                        onEmailChanged = {
                            userEmail = it
                            isEmailFormatValid = it.isValidEmail()
                            validationMessage = if (!isEmailFormatValid) "Uncorrected email" else ""
                        },
                        onClearClicked = {
                            userEmail = ""
                            isEmailFormatValid = true
                            validationMessage = ""
                        },
                    )

                    Text(
                        validationMessage,
                        modifier = Modifier
                            .padding(20.dp)
                            .alpha(if (validationMessage.isNotEmpty() && isEmailFormatValid) 1f else 0f)
                        ,
                    )

                    PrimaryButton("Login") {
                        validationMessage = if (userEmail.isEmpty() || !isEmailFormatValid) {
                            "Uncorrected email"
                        } else if (userEmail == testMail) {
                            "Already exists"
                        } else {
                            "Well done"
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainCheckBox() {
//    val isChecked: MutableState<Boolean> = remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

    Checkbox(
        checked = isChecked,
        onCheckedChange = {
            isChecked = it
        },
        modifier = Modifier
            .scale(scale = 2f)
            .padding(20.dp)
    )
}

@Composable
fun CheckEmailField(
    email: String,
    isEmailValid: Boolean,
    onEmailChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = {
            onEmailChanged(it)
        },
        shape = RoundedCornerShape(10.dp),
        placeholder = {
            Text("example@mail.com")
        },
        singleLine = true,
        label = {
            Text(
                text = if (isEmailValid) "Email" else "uncorrected email"
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    onClearClicked()
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "",
                )
            }
        },
        isError = !isEmailValid && email.isNotEmpty(),
    )
}

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return emailRegex.matches(this)
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: (String) -> Unit,
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        onClick = { onClick(text) },
        modifier = Modifier
            .height(56.dp)
            .padding(40.dp, 0.dp)
            .fillMaxWidth()
    ) {
        Text(
            text,
        )
    }
}