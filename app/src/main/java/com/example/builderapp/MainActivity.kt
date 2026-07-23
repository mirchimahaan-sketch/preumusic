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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Calculator App",
            style = MaterialTheme.typography.h6
        )

        Text(
            text = "Number 1: $num1",
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "Number 2: $num2",
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "Result: $result",
            style = MaterialTheme.typography.body1
        )

        Button(onClick = {
            num1 = "10"
            num2 = "20"
            result = (num1.toInt() + num2.toInt()).toString()
        }) {
            Text("Add")
        }

        Button(onClick = {
            num1 = "10"
            num2 = "20"
            result = (num1.toInt() - num2.toInt()).toString()
        }) {
            Text("Subtract")
        }

        Button(onClick = {
            num1 = "10"
            num2 = "20"
            result = (num1.toInt() * num2.toInt()).toString()
        }) {
            Text("Multiply")
        }

        Button(onClick = {
            num1 = "20"
            num2 = "2"
            result = (num1.toInt() / num2.toInt()).toString()
        }) {
            Text("Divide")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}