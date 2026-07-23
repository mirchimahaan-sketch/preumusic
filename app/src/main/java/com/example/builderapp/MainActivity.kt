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
    var result by remember { mutableStateOf("") }
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { operation = "+" }) { Text("+") }
            Button(onClick = { operation = "-" }) { Text("-") }
            Button(onClick = { operation = "*" }) { Text("*") }
            Button(onClick = { operation = "/" }) { Text("/") }
        }
        Button(onClick = {
            when (operation) {
                "+" -> result = (num1.toInt() + num2.toInt()).toString()
                "-" -> result = (num1.toInt() - num2.toInt()).toString()
                "*" -> result = (num1.toInt() * num2.toInt()).toString()
                "/" -> if (num2.toInt() != 0) result = (num1.toInt() / num2.toInt()).toString() else result = "Error"
                else -> result = "Select operation"
            }
        }) { Text("Calculate") }
        Text("Result: $result")
    }
}