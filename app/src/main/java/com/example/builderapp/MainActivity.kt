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
import androidx.compose.ui.unit.dp

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
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = number1,
            onValueChange = { number1 = it },
            label = { Text("Number 1") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = number2,
            onValueChange = { number2 = it },
            label = { Text("Number 2") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { operation = "+" }) { Text("+") }
            Button(onClick = { operation = "-" }) { Text("-") }
            Button(onClick = { operation = "*" }) { Text("*") }
            Button(onClick = { operation = "/" }) { Text("/") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (number1.isNotEmpty() && number2.isNotEmpty()) {
                val num1 = number1.toDouble()
                val num2 = number2.toDouble()
                when (operation) {
                    "+" -> result = (num1 + num2).toString()
                    "-" -> result = (num1 - num2).toString()
                    "*" -> result = (num1 * num2).toString()
                    "/" -> if (num2 != 0.0) result = (num1 / num2).toString() else result = "Error: Division by zero"
                    else -> result = "Error: No operation selected"
                }
            } else {
                result = "Error: Please enter both numbers"
            }
        }) { Text("Calculate") }
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