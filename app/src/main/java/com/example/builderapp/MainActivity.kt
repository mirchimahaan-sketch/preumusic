The error log provided does not directly relate to the Kotlin code, but rather to the GitHub Actions workflow and Node.js deprecation. However, I can provide a complete `MainActivity.kt` in Jetpack Compose for the "CapCulStor Premium" app.


package com.example.builderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CapCulStorPremiumApp()
        }
    }
}

@Composable
fun CapCulStorPremiumApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Text(text = "CapCulStor Premium")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CapCulStorPremiumApp()
}


To fix the GitHub Actions workflow error, you can update your workflow to use Node.js 24 instead of Node 20. You can do this by adding the following line to your workflow file:
yml
name: Android Build

on:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      - name: Set up Java
        uses: actions/setup-java@v4
      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v3
      - name: Run Gradle build
        run: |
          ./gradlew build
      - name: Run tests
        run: |
          ./gradlew test

Make sure to update the `actions/checkout@v4`, `actions/setup-java@v4`, and `gradle/actions/setup-gradle@v3` to the latest versions.

Also, you can add the following line to your workflow file to use Node.js 24:
yml
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '24'

This will ensure that your workflow uses Node.js 24 instead of Node 20.