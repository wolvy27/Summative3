package com.example.summative3.utils

open class CalculatorProcessor {
    open fun evaluateExpression(expression: String): Double {
        if (expression.isEmpty()) return 0.0

        val tokens = mutableListOf<String>()
        val buffer = StringBuilder()
        var i = 0

        while (i < expression.length) {
            when (val c = expression[i]) {
                '+', '-', '*', '/', '%', '(', ')' -> {
                    if (buffer.isNotEmpty()) {
                        tokens.add(buffer.toString())
                        buffer.clear()
                    }
                    if (c == '-' && (tokens.isEmpty() || tokens.last() == "(")) {
                        buffer.append(c)
                    } else {
                        tokens.add(c.toString())
                    }
                    i++
                }
                in '0'..'9', '.' -> {
                    buffer.append(c)
                    i++
                }
                else -> i++
            }
        }
        if (buffer.isNotEmpty()) tokens.add(buffer.toString())

        val postfix = infixToPostfix(tokens)

        return evaluatePostfix(postfix)
    }

    private fun infixToPostfix(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val operatorStack = ArrayDeque<String>()
        val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2, "%" to 2)

        for (token in tokens) {
            when {
                token.matches(Regex("-?\\d+\\.?\\d*")) -> output.add(token)
                token == "(" -> operatorStack.addLast(token)
                token == ")" -> {
                    while (operatorStack.last() != "(") {
                        output.add(operatorStack.removeLast())
                    }
                    operatorStack.removeLast()
                }
                else -> {
                    while (operatorStack.isNotEmpty() &&
                        operatorStack.last() != "(" &&
                        (precedence[operatorStack.last()] ?: 0) >= (precedence[token] ?: 0)) {
                        output.add(operatorStack.removeLast())
                    }
                    operatorStack.addLast(token)
                }
            }
        }
        output.addAll(operatorStack.reversed())
        return output
    }

    private fun evaluatePostfix(postfix: List<String>): Double {
        val stack = ArrayDeque<Double>()
        for (token in postfix) {
            when {
                token.matches(Regex("-?\\d+\\.?\\d*")) -> stack.addLast(token.toDouble())
                else -> {
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    stack.addLast(when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        "%" -> a % b
                        else -> throw IllegalArgumentException("Invalid operator")
                    })
                }
            }
        }
        return stack.single()
    }
}