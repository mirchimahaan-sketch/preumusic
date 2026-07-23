package com.example.builderapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
            Button(onClick = {
                operation = "+"
                result = (num1.toIntOrNull() ?: 0) + (num2.toIntOrNull() ?: 0).toString()
            }) {
                Text("+")
            }
            Button(onClick = {
                operation = "-"
                result = (num1.toIntOrNull() ?: 0) - (num2.toIntOrNull() ?: 0).toString()
            }) {
                Text("-")
            }
            Button(onClick = {
                operation = "*"
                result = (num1.toIntOrNull() ?: 0) * (num2.toIntOrNull() ?: 0).toString()
            }) {
                Text("*")
            }
            Button(onClick = {
                if (num2.toIntOrNull() != 0) {
                    operation = "/"
                    result = (num1.toIntOrNull() ?: 0) / (num2.toIntOrNull() ?: 0).toString()
                } else {
                    result = "Error: Division by zero"
                }
            }) {
                Text("/")
            }
        }
        Text(
            text = "Result: $result",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit
) {
    androidx.compose.material3.TextField(
        value = value,
        onValueChange = onValueChange,
        label = label
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}