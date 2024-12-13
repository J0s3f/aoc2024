package aockt.y2024

import io.github.jadarma.aockt.core.Solution
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.regex.Pattern


object Y2024D13 : Solution {
    override fun partOne(input: String): BigInteger {
        val machines = parseMachines(input)

        val results = machines.map(::solveMachine)

        return results.filterNotNull().sumOf { it }
    }

    override fun partTwo(input: String): BigInteger {
        val partTwoOffset = 10000000000000.toBigInteger()
        val machines = parseMachines(input).map { machine: Machine ->
            Machine(
                machine.aX,
                machine.aY,
                machine.bX,
                machine.bY,
                machine.prizeX + partTwoOffset,
                machine.prizeY + partTwoOffset
            )
        }

        val results = machines.map(::solveMachine)

        return results.filterNotNull().sumOf { it }
    }

    private fun BigDecimal.isWholeNumber() = stripTrailingZeros().scale() <= 0

    data class Solution(val x: BigDecimal, val y: BigDecimal)

    private fun solveEquations(machine: Machine): Solution {
        val aX = machine.aX.toBigDecimal()
        val aY = machine.aY.toBigDecimal()
        val bX = machine.bX.toBigDecimal()
        val bY = machine.bY.toBigDecimal()
        val priceX = machine.prizeX.toBigDecimal()
        val priceY = machine.prizeY.toBigDecimal()

        val det = aX * bY - aY * bX
        val x = (priceX * bY - priceY * bX).divide(det, 2, RoundingMode.HALF_UP)
        val y = (aX * priceY - aY * priceX).divide(det, 2, RoundingMode.HALF_UP)
        return Solution(x, y)
    }

    private fun solveMachine(machine: Machine): BigInteger? {
        val result = solveEquations(machine)
        return if (result.x.isWholeNumber() && result.y.isWholeNumber())
            (result.x * 3.toBigDecimal() + result.y).toBigInteger()
        else null
    }

    private fun parseMachines(input: String): List<Machine> {
        val machineRegex = Pattern.compile(
            "Button A: X\\+(?<aX>-?\\d+), Y\\+(?<aY>-?\\d+)\\s+" +
                    "Button B: X\\+(?<bX>-?\\d+), Y\\+(?<bY>-?\\d+)\\s+" +
                    "Prize: X=(?<prizeX>-?\\d+), Y=(?<prizeY>-?\\d+)"
        )

        return machineRegex.matcher(input).results().map {
            val aX = it.group("aX").toInt()
            val aY = it.group("aY").toInt()
            val bX = it.group("bX").toInt()
            val bY = it.group("bY").toInt()
            val prizeX = it.group("prizeX").toBigInteger()
            val prizeY = it.group("prizeY").toBigInteger()

            Machine(aX, aY, bX, bY, prizeX, prizeY)
        }.toList()
    }

    data class Machine(
        val aX: Int,
        val aY: Int,
        val bX: Int,
        val bY: Int,
        val prizeX: BigInteger,
        val prizeY: BigInteger
    ) {
        override fun toString(): String = "Button A: X+$aX, Y+$aY\nButton B: X+$bX, Y+$bY\nPrize: X=$prizeX, Y=$prizeY"
    }

}

