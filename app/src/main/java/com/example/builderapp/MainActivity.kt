package com.example.builderapp

import android.os.Bundle
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
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = number1,
            onValueChange = { number1 = it },
            label = { Text("Number 1") }
        )
        TextField(
            value = number2,
            onValueChange = { number2 = it },
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
                "+" -> result = (number1.toDouble() + number2.toDouble()).toString()
                "-" -> result = (number1.toDouble() - number2.toDouble()).toString()
                "*" -> result = (number1.toDouble() * number2.toDouble()).toString()
                "/" -> if (number2.toDouble() != 0.0) {
                    result = (number1.toDouble() / number2.toDouble()).toString()
                } else {
                    result = "Error: Division by zero"
                }
                else -> result = "Error: No operation selected"
            }
        }) { Text("Calculate") }
        Text(text = "Result: $result", style = MaterialTheme.typography.headlineSmall)
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