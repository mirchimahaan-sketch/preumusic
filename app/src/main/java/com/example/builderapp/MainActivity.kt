package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*

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
    val numbers = (0..9).toList()
    val operations = listOf("+", "-", "*", "/")
    var num1 by androidx.compose.runtime.mutableStateOf("")
    var num2 by androidx.compose.runtime.mutableStateOf("")
    var operation by androidx.compose.runtime.mutableStateOf("")
    var result by androidx.compose.runtime.mutableStateOf("")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Simple Calculator", style = MaterialTheme.typography.h6)
        Text(text = "Enter first number: ")
        Button(onClick = { num1 = "1" }) { Text("1") }
        Button(onClick = { num1 = "2" }) { Text("2") }
        Button(onClick = { num1 = "3" }) { Text("3") }
        Text(text = "Enter second number: ")
        Button(onClick = { num2 = "1" }) { Text("1") }
        Button(onClick = { num2 = "2" }) { Text("2") }
        Button(onClick = { num2 = "3" }) { Text("3") }
        Text(text = "Select operation: ")
        Button(onClick = { operation = "+" }) { Text("+") }
        Button(onClick = { operation = "-" }) { Text("-") }
        Button(onClick = { operation = "*" }) { Text("*") }
        Button(onClick = { operation = "/" }) { Text("/") }
        Button(onClick = {
            when (operation) {
                "+" -> result = (num1.toInt() + num2.toInt()).toString()
                "-" -> result = (num1.toInt() - num2.toInt()).toString()
                "*" -> result = (num1.toInt() * num2.toInt()).toString()
                "/" -> result = (num1.toInt() / num2.toInt()).toString()
                else -> result = "Invalid operation"
            }
        }) {
            Text("Calculate")
        }
        Text(text = "Result: $result")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}