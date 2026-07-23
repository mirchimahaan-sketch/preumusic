package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

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
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = result,
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            text = equation,
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "7",
                onClick = {
                    equation += "7"
                    result += "7"
                }
            )
            CalculatorButton(
                text = "8",
                onClick = {
                    equation += "8"
                    result += "8"
                }
            )
            CalculatorButton(
                text = "9",
                onClick = {
                    equation += "9"
                    result += "9"
                }
            )
            CalculatorButton(
                text = "/",
                onClick = {
                    equation += "/"
                    result += "/"
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
                    result += "4"
                }
            )
            CalculatorButton(
                text = "5",
                onClick = {
                    equation += "5"
                    result += "5"
                }
            )
            CalculatorButton(
                text = "6",
                onClick = {
                    equation += "6"
                    result += "6"
                }
            )
            CalculatorButton(
                text = "*",
                onClick = {
                    equation += "*"
                    result += "*"
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
                    result += "1"
                }
            )
            CalculatorButton(
                text = "2",
                onClick = {
                    equation += "2"
                    result += "2"
                }
            )
            CalculatorButton(
                text = "3",
                onClick = {
                    equation += "3"
                    result += "3"
                }
            )
            CalculatorButton(
                text = "-",
                onClick = {
                    equation += "-"
                    result += "-"
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
                    result += "0"
                }
            )
            CalculatorButton(
                text = ".",
                onClick = {
                    equation += "."
                    result += "."
                }
            )
            CalculatorButton(
                text = "=",
                onClick = {
                    try {
                        result = calculate(equation).toString()
                    } catch (e: Exception) {
                        result = "Error"
                    }
                }
            )
            CalculatorButton(
                text = "+",
                onClick = {
                    equation += "+"
                    result += "+"
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
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

fun calculate(equation: String): Double {
    return eval(equation)
}

fun eval(str: String): Double {
    return object : Any() {
        var pos = -1
        var ch = 0

        fun nextChar() {
            ch = if (++pos < str.length) str[pos].code else -1
        }

        fun eat(charToEat: Int) {
            while (ch == ' '.code) nextChar()
            if (ch == charToEat) nextChar() else throw RuntimeException()
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < str.length) throw RuntimeException()
            return x
        }

        fun parseExpression(): Double {
            var x = parseTerm()
            for (;;) {
                if (ch == '+'.code) {
                    nextChar()
                    x += parseTerm()
                } else if (ch == '-'.code) {
                    nextChar()
                    x -= parseTerm()
                } else {
                    return x
                }
            }
        }

        fun parseTerm(): Double {
            var x = parseFactor()
            for (;;) {
                if (ch == '*'.code) {
                    nextChar()
                    x *= parseFactor()
                } else if (ch == '/'.code) {
                    nextChar()
                    x /= parseFactor()
                } else {
                    return x
                }
            }
        }

        fun parseFactor(): Double {
            if (ch == '('.code) {
                nextChar()
                val x = parseExpression()
                eat(')'.code)
                return x
            }
            var x = 0.0
            var hasDecimal = false
            var hasDigit = false
            while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) {
                if (ch == '.'.code) {
                    if (hasDecimal) throw RuntimeException()
                    hasDecimal = true
                } else {
                    hasDigit = true
                }
                x = x * 10 + (ch - '0'.code)
                nextChar()
            }
            if (!hasDigit) throw RuntimeException()
            return x
        }
    }.parse()
}