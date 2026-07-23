package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.Row
import androidx.compose.material.TextField
import java.lang.Math

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        TextField(
            value = num1.value,
            onValueChange = { num1.value = it },
            label = { Text("Number 1") }
        )
        TextField(
            value = num2.value,
            onValueChange = { num2.value = it },
            label = { Text("Number 2") }
        )
        Row {
            Button(onClick = { operator.value = "+" }) { Text("+") }
            Button(onClick = { operator.value = "-" }) { Text("-") }
            Button(onClick = { operator.value = "*" }) { Text("*") }
            Button(onClick = { operator.value = "/" }) { Text("/") }
        }
        Button(onClick = {
            val n1 = num1.value.toDouble()
            val n2 = num2.value.toDouble()
            when (operator.value) {
                "+" -> result.value = (n1 + n2).toString()
                "-" -> result.value = (n1 - n2).toString()
                "*" -> result.value = (n1 * n2).toString()
                "/" -> result.value = if (n2 != 0.0) (n1 / n2).toString() else "Error"
            }
        }) { Text("Calculate") }
        Text(text = "Result: ${result.value}")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}