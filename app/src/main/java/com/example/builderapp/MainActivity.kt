Below is the full Jetpack Compose Kotlin code for a simple calculator app named "Calculator Ultra" under the package `com.example.builderapp`. The app includes basic arithmetic operations (addition, subtraction, multiplication, and division) and a clean UI.


package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    var input1 by remember { mutableStateOf(TextFieldValue("")) }
    var input2 by remember { mutableStateOf(TextFieldValue("")) }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Calculator Ultra",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input fields for numbers
        BasicTextField(
            value = input1,
            onValueChange = { input1 = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (input1.text.isEmpty()) {
                        Text("Enter first number")
                    }
                    innerTextField()
                }
            }
        )

        BasicTextField(
            value = input2,
            onValueChange = { input2 = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (input2.text.isEmpty()) {
                        Text("Enter second number")
                    }
                    innerTextField()
                }
            }
        )

        // Buttons for operations
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                result = calculate(input1.text, input2.text, "+")
            }) {
                Text("+")
            }
            Button(onClick = {
                result = calculate(input1.text, input2.text, "-")
            }) {
                Text("-")
            }
            Button(onClick = {
                result = calculate(input1.text, input2.text, "*")
            }) {
                Text("×")
            }
            Button(onClick = {
                result = calculate(input1.text, input2.text, "/")
            }) {
                Text("÷")
            }
        }

        // Display result
        Text(
            text = "Result: $result",
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

fun calculate(input1: String, input2: String, operation: String): String {
    val num1 = input1.toDoubleOrNull()
    val num2 = input2.toDoubleOrNull()

    if (num1 == null || num2 == null) {
        return "Invalid input"
    }

    return when (operation) {
        "+" -> (num1 + num2).toString()
        "-" -> (num1 - num2).toString()
        "*" -> (num1 * num2).toString()
        "/" -> if (num2 != 0.0) (num1 / num2).toString() else "Cannot divide by zero"
        else -> "Unknown operation"
    }
}


### Features:
1. **Input Fields**: Two text fields for entering numbers.
2. **Operation Buttons**: Buttons for addition, subtraction, multiplication, and division.
3. **Result Display**: Displays the result of the calculation.
4. **Error Handling**: Handles invalid inputs and division by zero.

### Notes:
- The `BasicTextField` is used for input fields with a custom decoration.
- The `calculate` function performs the arithmetic operations and handles errors like invalid input or division by zero.
- The UI is responsive and clean, with proper padding and alignment.