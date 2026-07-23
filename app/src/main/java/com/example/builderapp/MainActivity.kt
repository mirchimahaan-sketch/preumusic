package com.example.builderapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalcultooorTheme {
                CalcultooorApp()
            }
        }
    }
}

@Composable
fun CalcultooorApp() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
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
            label = { Text("Number 1") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Number 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                operation = "+"
                calculate(num1, num2, operation, { result = it })
            }) {
                Text("+")
            }

            Button(onClick = {
                operation = "-"
                calculate(num1, num2, operation, { result = it })
            }) {
                Text("-")
            }

            Button(onClick = {
                operation = "*"
                calculate(num1, num2, operation, { result = it })
            }) {
                Text("*")
            }

            Button(onClick = {
                operation = "/"
                calculate(num1, num2, operation, { result = it })
            }) {
                Text("/")
            }
        }

        Text("Result: $result")
    }
}

fun calculate(num1: String, num2: String, operation: String, callback: (String) -> Unit) {
    try {
        val n1 = num1.toDouble()
        val n2 = num2.toDouble()

        when (operation) {
            "+" -> callback((n1 + n2).toString())
            "-" -> callback((n1 - n2).toString())
            "*" -> callback((n1 * n2).toString())
            "/" -> {
                if (n2 != 0.0) {
                    callback((n1 / n2).toString())
                } else {
                    callback("Error: Division by zero")
                }
            }
            else -> callback("Error: Invalid operation")
        }
    } catch (e: Exception) {
        callback("Error: Invalid input")
        Log.e("Calcultooor", e.toString())
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalcultooorTheme {
        CalcultooorApp()
    }
}