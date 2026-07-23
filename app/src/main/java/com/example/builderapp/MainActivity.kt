package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var text by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf("0") }
    var operation by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = text, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { text += "7" }) { Text("7") }
            Button(onClick = { text += "8" }) { Text("8") }
            Button(onClick = { text += "9" }) { Text("9") }
            Button(onClick = { text += "/" }) { Text("/") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { text += "4" }) { Text("4") }
            Button(onClick = { text += "5" }) { Text("5") }
            Button(onClick = { text += "6" }) { Text("6") }
            Button(onClick = { text += "*" }) { Text("*") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { text += "1" }) { Text("1") }
            Button(onClick = { text += "2" }) { Text("2") }
            Button(onClick = { text += "3" }) { Text("3") }
            Button(onClick = { text += "-" }) { Text("-") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { text += "0" }) { Text("0") }
            Button(onClick = { text += "." }) { Text(".") }
            Button(onClick = { 
                try {
                    result = calculateResult(text)
                    text = result
                } catch (e: Exception) {
                    text = "Error"
                }
            }) { Text("=") }
            Button(onClick = { text += "+" }) { Text("+") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { text = "0" }) { Text("Clear") }
    }
}

fun calculateResult(text: String): String {
    return try {
        val result = eval(text)
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
            if (pos >= str.length) {
                ch = ' '
            } else {
                ch = str[pos]
            }
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
            if (pos < str.length) throw RuntimeException("Unexpected: " + ch)
            return x
        }

        fun parseExpression(): Double {
            var x = parseTerm()
            for (;;) {
                if (eat('+')) x += parseTerm() // addition
                else if (eat('-')) x -= parseTerm() // subtraction
                else return x
            }
        }

        fun parseTerm(): Double {
            var x = parseFactor()
            for (;;) {
                if (eat('*')) x *= parseFactor() // multiplication
                else if (eat('/')) x /= parseFactor() // division
                else return x
            }
        }

        fun parseFactor(): Double {
            if (eat('+')) return parseFactor() // unary plus
            if (eat('-')) return -parseFactor() // unary minus

            if (eat('(')) { // parentheses
                val x = parseExpression()
                if (!eat(')')) throw RuntimeException("Unexpected: " + ch)
                return x
            }

            var x = 0.0
            var hasDecimal = false
            var decimalPlaces = 0.0
            while (ch in '0'..'9' || ch == '.') {
                if (ch == '.') {
                    hasDecimal = true
                    nextChar()
                } else {
                    val digit = (ch - '0').toDouble()
                    if (hasDecimal) {
                        decimalPlaces++
                        x += digit / Math.pow(10.0, decimalPlaces)
                    } else {
                        x = x * 10 + digit
                    }
                    nextChar()
                }
            }
            return x
        }
    }.parse()
}