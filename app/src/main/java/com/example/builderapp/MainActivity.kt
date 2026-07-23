The error log provided does not directly point to an issue with the `MainActivity.kt` file. However, it seems like there are issues with the Gradle build and caching. To fix the Gradle build issue, you can try the following steps:

1. Clean and rebuild the project.
2. Invalidate the Gradle cache.
3. Update the Gradle version.

However, since the original prompt was to create a calculator app using Jetpack Compose, I will provide a basic implementation of a calculator app.


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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (number1.isNotEmpty() && number2.isNotEmpty()) {
                    result = (number1.toInt() + number2.toInt()).toString()
                }
            }) {
                Text(text = "+")
            }
            Button(onClick = {
                if (number1.isNotEmpty() && number2.isNotEmpty()) {
                    result = (number1.toInt() - number2.toInt()).toString()
                }
            }) {
                Text(text = "-")
            }
            Button(onClick = {
                if (number1.isNotEmpty() && number2.isNotEmpty()) {
                    result = (number1.toInt() * number2.toInt()).toString()
                }
            }) {
                Text(text = "*")
            }
            Button(onClick = {
                if (number1.isNotEmpty() && number2.isNotEmpty()) {
                    if (number2.toInt() != 0) {
                        result = (number1.toInt() / number2.toInt()).toString()
                    }
                }
            }) {
                Text(text = "/")
            }
        }
        Text(
            text = "Result: $result",
            style = MaterialTheme.typography.headlineMedium
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


This code creates a basic calculator app with two input fields for numbers and four buttons for basic arithmetic operations. The result is displayed below the buttons.