package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.builderapp.ui.theme.BuilderappTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuilderappTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf("+") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Calculator", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Number 1:")
        androidx.compose.material.TextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Number 1") }
        )
        Text(text = "Operator:")
        androidx.compose.material3.ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = true,
            onExpandedChange = { }
        ) {
            androidx.compose.material3.ExposedDropdownMenuBoxScope().MenuItem(
                onClick = { operator = "+" }
            ) {
                Text("+")
            }
            androidx.compose.material3.ExposedDropdownMenuBoxScope().MenuItem(
                onClick = { operator = "-" }
            ) {
                Text("-")
            }
            androidx.compose.material3.ExposedDropdownMenuBoxScope().MenuItem(
                onClick = { operator = "*" }
            ) {
                Text("*")
            }
            androidx.compose.material3.ExposedDropdownMenuBoxScope().MenuItem(
                onClick = { operator = "/" }
            ) {
                Text("/")
            }
        }
        Text(text = "Number 2:")
        androidx.compose.material.TextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Number 2") }
        )
        Button(onClick = {
            when (operator) {
                "+" -> result = (num1.toDouble() + num2.toDouble()).toString()
                "-" -> result = (num1.toDouble() - num2.toDouble()).toString()
                "*" -> result = (num1.toDouble() * num2.toDouble()).toString()
                "/" -> result = if (num2.toDouble() != 0.0) (num1.toDouble() / num2.toDouble()).toString() else "Error"
                else -> result = "Error"
            }
        }) {
            Text("Calculate")
        }
        Text(text = "Result: $result")
    }
}
