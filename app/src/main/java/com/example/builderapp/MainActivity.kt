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
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = number1,
            onValueChange = { number1 = it },
            label = { Text("Number 1") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = number2,
            onValueChange = { number2 = it },
            label = { Text("Number 2") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {
                operation = "+"
                result = (number1.toDouble() + number2.toDouble()).toString()
            }) {
                Text("+")
            }

            Button(onClick = {
                operation = "-"
                result = (number1.toDouble() - number2.toDouble()).toString()
            }) {
                Text("-")
            }

            Button(onClick = {
                operation = "*"
                result = (number1.toDouble() * number2.toDouble()).toString()
            }) {
                Text("*")
            }

            Button(onClick = {
                operation = "/"
                if (number2 != "0") {
                    result = (number1.toDouble() / number2.toDouble()).toString()
                } else {
                    result = "Error: Division by zero"
                }
            }) {
                Text("/")
            }
        }

        Text(
            text = "Result: $result",
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            number1 = ""
            number2 = ""
            result = ""
            operation = ""
        }) {
            Text("Clear")
        }
    }
}