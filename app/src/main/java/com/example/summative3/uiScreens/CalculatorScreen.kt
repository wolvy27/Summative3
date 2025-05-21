package com.example.summative3.uiScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.summative3.utils.CalculatorProcessor // Import the new class

@Composable
fun CalculatorScreen(navController: NavHostController) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Create an instance of CalculatorProcessor
    val calculatorProcessor = remember { CalculatorProcessor() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Display input and result
        Text(
            text = input,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = result,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.displaySmall
        )

        // Buttons grid layout
        val buttons = listOf(
            listOf("AC", "DEL", "MOD", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=", "")
        )

        for (row in buttons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (button in row) {
                    if (button.isNotEmpty()) {
                        CalculatorButton(
                            text = button,
                            modifier = Modifier.weight(1f)
                        ) {
                            when (button) {
                                "AC" -> {
                                    input = ""
                                    result = "0"
                                    errorMessage = null
                                }
                                "DEL" -> {
                                    if (input.isNotEmpty()) input = input.dropLast(1)
                                }
                                "=" -> {
                                    try {
                                        // Use the instance here
                                        result = calculatorProcessor.evaluateExpression(input).toString()
                                        errorMessage = null
                                    } catch (e: Exception) {
                                        errorMessage = "Invalid expression"
                                        result = "Error"
                                    }
                                }
                                else -> input += when (button) {
                                    "MOD" -> "%"
                                    else -> button
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}


@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(64.dp)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}