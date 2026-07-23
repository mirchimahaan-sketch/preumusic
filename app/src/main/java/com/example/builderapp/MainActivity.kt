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
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = equation,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = result,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { equation += "7"; result += "7" }) { Text("7") }
            Button(onClick = { equation += "8"; result += "8" }) { Text("8") }
            Button(onClick = { equation += "9"; result += "9" }) { Text("9") }
            Button(onClick = { equation += "/"; result += "/" }) { Text("/") }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { equation += "4"; result += "4" }) { Text("4") }
            Button(onClick = { equation += "5"; result += "5" }) { Text("5") }
            Button(onClick = { equation += "6"; result += "6" }) { Text("6") }
            Button(onClick = { equation += "*"; result += "*" }) { Text("*") }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { equation += "1"; result += "1" }) { Text("1") }
            Button(onClick = { equation += "2"; result += "2" }) { Text("2") }
            Button(onClick = { equation += "3"; result += "3" }) { Text("3") }
            Button(onClick = { equation += "-"; result += "-" }) { Text("-") }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { equation += "0"; result += "0" }) { Text("0") }
            Button(onClick = { equation += "."; result += "." }) { Text(".") }
            Button(onClick = { equation += "="; result = calculate(equation) }) { Text("=") }
            Button(onClick = { equation += "+"; result += "+" }) { Text("+") }
        }

        Button(onClick = { equation = ""; result = "" }) { Text("Clear") }
    }
}

fun calculate(equation: String): String {
    return try {
        val result = equation.replace("=", "").replace(" ", "")
        val eval = Eval()
        eval.evaluate(result).toString()
    } catch (e: Exception) {
        "Error"
    }
}

class Eval {
    fun evaluate(str: String): Double {
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
                while (true) {
                    if (ch == '+'.code) {
                        eat('+'.code)
                        x += parseTerm()
                    } else if (ch == '-'.code) {
                        eat('-'.code)
                        x -= parseTerm()
                    } else {
                        return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (ch == '*'.code) {
                        eat('*'.code)
                        x *= parseFactor()
                    } else if (ch == '/'.code) {
                        eat('/'.code)
                        x /= parseFactor()
                    } else {
                        return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (ch == '('.code) {
                    eat('('.code)
                    val x = parseExpression()
                    eat(')'.code)
                    return x
                }
                var x = 0.0
                while (ch in '0'.code..'9'.code || ch == '.'.code) {
                    x = x * 10 + (ch - '0'.code)
                    nextChar()
                }
                return x
            }
        }.parse()
    }
}