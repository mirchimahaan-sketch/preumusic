package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = expression,
            onValueChange = { expression = it },
            label = { Text("Expression") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { expression += "7" }) { Text("7") }
            Button(onClick = { expression += "8" }) { Text("8") }
            Button(onClick = { expression += "9" }) { Text("9") }
            Button(onClick = { expression += "/" }) { Text("/") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { expression += "4" }) { Text("4") }
            Button(onClick = { expression += "5" }) { Text("5") }
            Button(onClick = { expression += "6" }) { Text("6") }
            Button(onClick = { expression += "*" }) { Text("*") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { expression += "1" }) { Text("1") }
            Button(onClick = { expression += "2" }) { Text("2") }
            Button(onClick = { expression += "3" }) { Text("3") }
            Button(onClick = { expression += "-" }) { Text("-") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { expression += "0" }) { Text("0") }
            Button(onClick = { expression += "." }) { Text(".") }
            Button(onClick = { expression += "=" }) { Text("=") }
            Button(onClick = { expression += "+" }) { Text("+") }
        }

        Button(onClick = {
            try {
                result = calculateExpression(expression)
            } catch (e: Exception) {
                result = "Error"
            }
        }) { Text("Calculate") }

        Text(text = "Result: $result")
    }
}

fun calculateExpression(expression: String): String {
    return when {
        expression.isEmpty() -> "0"
        expression == "=" -> "0"
        else -> try {
            val result = eval(expression.replace("=", ""))
            result.toString()
        } catch (e: Exception) {
            "Error"
        }
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
                if (!eat(')')) throw RuntimeException("Missing ')'")
                return x
            }

            var x = 0.0
            if (ch in '0'..'9' || ch == '.') {
                var strNum = ""
                while (ch in '0'..'9' || ch == '.') {
                    strNum += ch
                    nextChar()
                }
                x = strNum.toDouble()
            }

            return x
        }
    }.parse()
}