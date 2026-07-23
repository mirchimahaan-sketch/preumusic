package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Number 1") }
        )
        TextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Number 2") }
        )
        Button(onClick = {
            try {
                val n1 = num1.toInt()
                val n2 = num2.toInt()
                result = (n1 + n2).toString()
            } catch (e: Exception) {
                result = "Invalid input"
            }
        }) {
            Text("Calculate")
        }
        Text(text = "Result: $result")
    }
}