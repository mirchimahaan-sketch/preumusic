package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppUI()
                }
            }
        }
    }
}

@Composable
fun AppUI() {
    var textFieldState by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(textFieldState) {
        isButtonEnabled = textFieldState.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = textFieldState,
            onValueChange = { textFieldState = it },
            label = { Text("Enter your text") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Button click logic */ },
            enabled = isButtonEnabled
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Capculstor Premium")
        }
    }
}