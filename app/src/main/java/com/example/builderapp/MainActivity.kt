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
                    AppUI()
                }
            }
        }
    }
}

@Composable
fun AppUI() {
    var result by remember { mutableStateOf("") }
    var currentNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = result,
            style = MaterialTheme.typography.displayLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { currentNumber += "7" }) { Text("7") }
            Button(onClick = { currentNumber += "8" }) { Text("8") }
            Button(onClick = { currentNumber += "9" }) { Text("9") }
            Button(onClick = { currentNumber += "/" }) { Text("/") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { currentNumber += "4" }) { Text("4") }
            Button(onClick = { currentNumber += "5" }) { Text("5") }
            Button(onClick = { currentNumber += "6" }) { Text("6") }
            Button(onClick = { currentNumber += "*" }) { Text("*") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { currentNumber += "1" }) { Text("1") }
            Button(onClick = { currentNumber += "2" }) { Text("2") }
            Button(onClick = { currentNumber += "3" }) { Text("3") }
            Button(onClick = { currentNumber += "-" }) { Text("-") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { currentNumber += "0" }) { Text("0") }
            Button(onClick = { currentNumber += "." }) { Text(".") }
            Button(onClick = { currentNumber += "=" }) { Text("=") }
            Button(onClick = { currentNumber += "+" }) { Text("+") }
        }
        Button(onClick = {
            try {
                result = calculate(currentNumber)
                currentNumber = ""
            } catch (e: Exception) {
                result = "Error"
            }
        }) { Text("Calculate") }
        Button(onClick = {
            currentNumber = ""
            result = ""
        }) { Text("Clear") }
    }
}

fun calculate(expression: String): String {
    return when {
        expression.contains("+") -> {
            val parts = expression.split("+")
            (parts[0].toDouble() + parts[1].toDouble()).toString()
        }
        expression.contains("-") -> {
            val parts = expression.split("-")
            (parts[0].toDouble() - parts[1].toDouble()).toString()
        }
        expression.contains("*") -> {
            val parts = expression.split("*")
            (parts[0].toDouble() * parts[1].toDouble()).toString()
        }
        expression.contains("/") -> {
            val parts = expression.split("/")
            (parts[0].toDouble() / parts[1].toDouble()).toString()
        }
        else -> "Invalid expression"
    }
}

@Preview
@Composable
fun PreviewAppUI() {
    MaterialTheme {
        AppUI()
    }
}