package com.example.summative3

import com.example.summative3.utils.CalculatorProcessor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class CalculatorProcessorTests {

    private lateinit var mockCalculatorProcessor: CalculatorProcessor
    private val realProcessor = CalculatorProcessor()

    @Before
    fun setup() {
        // Arrange
        mockCalculatorProcessor = mock(CalculatorProcessor::class.java)
    }

    @Test
    fun testBasicAdditionOperationWithMockito() {
        // Arrange
        val expression = "2+3"
        val expectedResult = 5.0
        `when`(mockCalculatorProcessor.evaluateExpression(expression)).thenReturn(expectedResult)

        // Act
        val result = mockCalculatorProcessor.evaluateExpression(expression)

        // Assert
        assertEquals(expectedResult, result, 0.001)
        verify(mockCalculatorProcessor, times(1)).evaluateExpression(expression)
    }

    @Test
    fun testSequentialOperationsWithoutPrecedenceWithMockito() {
        // Arrange
        val expression = "2+3*4"
        val expectedResult = 14.00
        `when`(mockCalculatorProcessor.evaluateExpression(expression)).thenReturn(expectedResult)

        // Act
        val result = mockCalculatorProcessor.evaluateExpression(expression)

        // Assert
        assertEquals(expectedResult, result, 0.001)
        verify(mockCalculatorProcessor, times(1)).evaluateExpression(expression)
    }

    @Test
    fun testDivisionOperationWithMockito() {
        // Arrange
        val expression = "50/10"
        val expectedResult = 5.0
        `when`(mockCalculatorProcessor.evaluateExpression(expression)).thenReturn(expectedResult)

        // Act
        val result = mockCalculatorProcessor.evaluateExpression(expression)

        // Assert
        assertEquals(expectedResult, result, 0.001)
        verify(mockCalculatorProcessor, times(1)).evaluateExpression(expression)
    }
}