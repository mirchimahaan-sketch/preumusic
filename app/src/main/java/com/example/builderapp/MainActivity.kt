package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CalculatorUI()
                }
            }
        }
    }
}

@Composable
fun CalculatorUI() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Number 1") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Number 2") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                operation = "+"
                result = (num1.toIntOrNull() ?: 0) + (num2.toIntOrNull() ?: 0).toString()
            }) {
                Text(text = "+")
            }
            Button(onClick = {
                operation = "-"
                result = (num1.toIntOrNull() ?: 0) - (num2.toIntOrNull() ?: 0).toString()
            }) {
                Text(text = "-")
            }
            Button(onClick = {
                operation = "*"
                result = (num1.toIntOrNull() ?: 0) * (num2.toIntOrNull() ?: 0).toString()
            }) {
                Text(text = "*")
            }
            Button(onClick = {
                operation = "/"
                if (num2.toIntOrNull() != 0) {
                    result = (num1.toIntOrNull() ?: 0) / (num2.toIntOrNull() ?: 0).toString()
                } else {
                    result = "Error: Division by zero"
                }
            }) {
                Text(text = "/")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Result: $result")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        CalculatorUI()
    }
}