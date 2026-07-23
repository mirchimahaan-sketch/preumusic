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
    var equation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = result,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = equation,
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "7",
                onClick = {
                    equation += "7"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "8",
                onClick = {
                    equation += "8"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "9",
                onClick = {
                    equation += "9"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "/",
                onClick = {
                    equation += "/"
                    result = calculateResult(equation)
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "4",
                onClick = {
                    equation += "4"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "5",
                onClick = {
                    equation += "5"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "6",
                onClick = {
                    equation += "6"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "*",
                onClick = {
                    equation += "*"
                    result = calculateResult(equation)
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "1",
                onClick = {
                    equation += "1"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "2",
                onClick = {
                    equation += "2"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "3",
                onClick = {
                    equation += "3"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "-",
                onClick = {
                    equation += "-"
                    result = calculateResult(equation)
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "0",
                onClick = {
                    equation += "0"
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = ".",
                onClick = {
                    equation += "."
                    result = calculateResult(equation)
                }
            )
            CalculatorButton(
                text = "=",
                onClick = {
                    result = calculateResult(equation)
                    equation = ""
                }
            )
            CalculatorButton(
                text = "+",
                onClick = {
                    equation += "+"
                    result = calculateResult(equation)
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "Clear",
                onClick = {
                    equation = ""
                    result = ""
                }
            )
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
    ) {
        Text(text = text)
    }
}

fun calculateResult(equation: String): String {
    return try {
        val result = eval(equation)
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

fun eval(str: String): Double {
    return object : Any() {
        var pos = -1
        var ch: Char = ' '

        fun nextChar() {
            pos++
            if (pos >= str.length) ch = ' '
            else ch = str[pos]
        }

        fun eat(charToEat: Char): Boolean {
            while (ch == ' ') nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < str.length) throw RuntimeException("Unexpected: $ch")
            return x
        }

        fun parseExpression(): Double {
            var x = parseTerm()
            for (;;) {
                if      (eat('+')) x += parseTerm() // addition
                else if (eat('-')) x -= parseTerm() // subtraction
                else return x
            }
        }

        fun parseTerm(): Double {
            var x = parseFactor()
            for (;;) {
                if      (eat('*')) x *= parseFactor() // multiplication
                else if (eat('/')) x /= parseFactor() // division
                else return x
            }
        }

        fun parseFactor(): Double {
            if (eat('+')) return parseFactor() // unary plus
            if (eat('-')) return -parseFactor() // unary minus

            if (eat('(')) { // parentheses
                val x = parseExpression()
                eat(')')
                return x
            }

            var x = 0.0
            while (ch in '0'..'9' || ch == '.') {
                if (ch == '.') {
                    nextChar()
                    var divisor = 1.0
                    while (ch in '0'..'9') {
                        x += (ch - '0') / (divisor * 10)
                        nextChar()
                        divisor *= 10
                    }
                } else {
                    x = x * 10 + (ch - '0')
                    nextChar()
                }
            }

            return x
        }
    }.parse()
}

@Preview
@Composable
fun PreviewAppUI() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppUI()
        }
    }
}