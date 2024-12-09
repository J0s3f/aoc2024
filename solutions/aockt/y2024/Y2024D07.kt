package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D07 : Solution {
    override fun partOne(input: String): Long {
        val equations = parse(input)
        val validEquations = equations.filter { it.equation.isPossible(it.testValue) }
        return validEquations.sumOf { it.testValue }
    }

    override fun partTwo(input: String): Long {
        val equations = parse(input)
        val validEquations = equations.filter { it.equation.isPossibleWithConcat(it.testValue) }
        return validEquations.sumOf { it.testValue }
    }


    private fun parse(input: String): List<Equation> {
        return input.trim().lines().map { line ->
            val (testValue, equation) = line.split(": ")
            Equation(testValue.toLong(), EquationTree(equation.split(" ")))
        }
    }

    data class Equation(val testValue: Long, val equation: EquationTree)

    class EquationTree(val left: Long, private val right: EquationTree?) {
        constructor(values: List<String>) : this(
            values.first().toLong(),
            if (values.size > 1) EquationTree(values.drop(1)) else null
        )

        fun isPossible(testValue: Long): Boolean {
            if (right == null) {
                return left == testValue
            }
            return sequenceOf(
                right.isPossible(testValue, left, EquationPart.Add),
                right.isPossible(testValue, left, EquationPart.Multiply)
            ).any { it }
        }

        private fun isPossible(testValue: Long, partialValue: Long): Boolean {
            return sequenceOf(
                isPossible(testValue, partialValue, EquationPart.Add),
                isPossible(testValue, partialValue, EquationPart.Multiply)
            ).any { it }
        }

        private fun isPossible(testValue: Long, partialValue: Long, operator: EquationPart.Operator): Boolean {
            if (right == null) {
                return when (operator) {
                    EquationPart.Add -> partialValue + left == testValue
                    EquationPart.Multiply -> partialValue * left == testValue
                    else -> {
                        throw IllegalStateException("Invalid operator for part one")
                    }
                }
            }
            return when (operator) {
                EquationPart.Add -> right.isPossible(testValue, partialValue + left)
                EquationPart.Multiply -> right.isPossible(testValue, partialValue * left)
                else -> {
                    throw IllegalStateException("Invalid operator for part one")
                }
            }
        }

        fun isPossibleWithConcat(testValue: Long): Boolean {
            if (right == null) {
                return left == testValue
            }
            return sequenceOf(
                right.isPossibleWithConcat(testValue, left, EquationPart.Add),
                right.isPossibleWithConcat(testValue, left, EquationPart.Multiply),
                right.isPossibleWithConcat(testValue, left, EquationPart.Concat)
            ).any { it }
        }

        private fun isPossibleWithConcat(testValue: Long, partialValue: Long): Boolean {
            return sequenceOf(
                isPossibleWithConcat(testValue, partialValue, EquationPart.Add),
                isPossibleWithConcat(testValue, partialValue, EquationPart.Multiply),
                isPossibleWithConcat(testValue, partialValue, EquationPart.Concat)
            ).any { it }
        }

        private fun isPossibleWithConcat(
            testValue: Long,
            partialValue: Long,
            operator: EquationPart.Operator
        ): Boolean {
            if (right == null) {
                return when (operator) {
                    EquationPart.Add -> partialValue + left == testValue
                    EquationPart.Multiply -> partialValue * left == testValue
                    EquationPart.Concat -> (partialValue.toString() + left.toString()).toLong() == testValue
                }
            }
            return when (operator) {
                EquationPart.Add -> right.isPossibleWithConcat(testValue, partialValue + left)
                EquationPart.Multiply -> right.isPossibleWithConcat(testValue, partialValue * left)
                EquationPart.Concat -> right.isPossibleWithConcat(
                    testValue,
                    (partialValue.toString() + left.toString()).toLong()
                )
            }
        }


        sealed class EquationPart {
            sealed class Operator : EquationPart()
            data object Multiply : Operator()
            data object Add : Operator()
            data object Concat : Operator()
        }
    }
}
