Below is the full implementation of a simple calculator app in Jetpack Compose using the provided base structure. The app includes basic arithmetic operations (addition, subtraction, multiplication, and division) and a clear button.


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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calculator Pro",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BasicTextField(
            value = input1,
            onValueChange = { input1 = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                ) {
                    if (input1.text.isEmpty()) {
                        Text("Enter first number", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
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
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                ) {
                    if (input2.text.isEmpty()) {
                        Text("Enter second number", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                result = try {
                    (input1.text.toDouble() + input2.text.toDouble()).toString()
                } catch (e: Exception) {
                    "Invalid Input"
                }
            }) {
                Text("+")
            }

            Button(onClick = {
                result = try {
                    (input1.text.toDouble() - input2.text.toDouble()).toString()
                } catch (e: Exception) {
                    "Invalid Input"
                }
            }) {
                Text("-")
            }

            Button(onClick = {
                result = try {
                    (input1.text.toDouble() * input2.text.toDouble()).toString()
                } catch (e: Exception) {
                    "Invalid Input"
                }
            }) {
                Text("×")
            }

            Button(onClick = {
                result = try {
                    if (input2.text.toDouble() != 0.0) {
                        (input1.text.toDouble() / input2.text.toDouble()).toString()
                    } else {
                        "Cannot divide by zero"
                    }
                } catch (e: Exception) {
                    "Invalid Input"
                }
            }) {
                Text("÷")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            input1 = TextFieldValue("")
            input2 = TextFieldValue("")
            result = ""
        }) {
            Text("Clear")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Result: $result",
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}


### Features:
1. **Input Fields**: Two text fields for entering numbers.
2. **Operations**: Buttons for addition, subtraction, multiplication, and division.
3. **Clear Button**: Resets the inputs and result.
4. **Result Display**: Shows the result of the operation or an error message for invalid input.

### Notes:
- The app uses `BasicTextField` for input fields with custom styling.
- Input validation is handled to ensure proper numeric input.
- Division by zero is explicitly handled to avoid crashes.