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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.pow

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
    val num1 = remember { mutableStateOf("") }
    val num2 = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("") }
    val operator = remember { mutableStateOf("+") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Calculator", style = MaterialTheme.typography.h6)
        Text(text = "Number 1: ${num1.value}", modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Number 2: ${num2.value}", modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Operator: ${operator.value}", modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Result: ${result.value}", modifier = Modifier.padding(bottom = 8.dp))
        Button(onClick = { num1.value = "5" }) { Text("5") }
        Button(onClick = { num2.value = "3" }) { Text("3") }
        Button(onClick = { operator.value = "+" }) { Text("+") }
        Button(onClick = { operator.value = "-" }) { Text("-") }
        Button(onClick = {
            when (operator.value) {
                "+" -> result.value = (num1.value.toInt() + num2.value.toInt()).toString()
                "-" -> result.value = (num1.value.toInt() - num2.value.toInt()).toString()
                else -> result.value = "Error"
            }
        }) { Text("Calculate") }
    }
}
