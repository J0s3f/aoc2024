package aockt.y2024

import aockt.utils.CharGrid
import aockt.utils.Point
import io.github.jadarma.aockt.core.Solution
import kotlin.math.abs

object Y2024D21 : Solution {
    override fun partOne(input: String): Long = calculateComplexity(input.lines(), 2)

    override fun partTwo(input: String): Long = calculateComplexity(input.lines(), 25)

    private val numericKeypad = CharGrid.fromString(
        """
        789
        456
        123
        .0A
        """.trimIndent()
    )

    private val directionalKeypad = CharGrid.fromString(
        """
        .^A
        <v>
        """.trimIndent()
    )

    private fun CharGrid.isValidPath(path: String, start: Point): Boolean {
        var current = start
        return path.all { dir ->
            current += when (dir) {
                '^' -> Point(0, 1)
                'v' -> Point(0, -1)
                '<' -> Point(-1, 0)
                '>' -> Point(1, 0)
                else -> error("Invalid direction $dir")
            }
            this.isValidPosition(current) && this[current] != '.'
        }
    }

    private data class DpParams(val sequence: String, val maxDepth: Int, val currentDepth: Int)

    private val cache = mutableMapOf<DpParams, Long>()
    private fun dp(params: DpParams): Long {
        return cache.getOrPut(params) {
            val (sequence, maxDepth, currentDepth) = params
            val keypad = if (currentDepth == 0) numericKeypad else directionalKeypad
            var pos = keypad.findChar('A')!!
            var sum = 0L

            sequence.forEach { char ->
                val next = keypad.findChar(char)!!
                val paths = generatePaths(pos, next)
                    .filter { path -> keypad.isValidPath(path, pos) }
                    .map { "${it}A" }
                    .ifEmpty { listOf("A") }

                sum += if (currentDepth == maxDepth) {
                    paths.minOf { it.length }.toLong()
                } else {
                    paths.minOf { dp(DpParams(it, maxDepth, currentDepth + 1)) }
                }
                pos = next
            }
            sum
        }
    }

    private fun generatePaths(from: Point, to: Point): List<String> {
        val dx = to.x - from.x
        val dy = to.y - from.y

        fun generateDirectionalSequence(dx: Int, dy: Int): List<String> {
            val result = mutableListOf<String>()

            fun generate(remainingH: Int, remainingV: Int, current: String) {
                if (remainingH == 0 && remainingV == 0) {
                    result.add(current)
                    return
                }
                if (remainingH != 0) {
                    generate(remainingH - 1, remainingV, current + if (dx < 0) "<" else ">")
                }
                if (remainingV != 0) {
                    generate(remainingH, remainingV - 1, current + if (dy < 0) "v" else "^")
                }
            }

            generate(abs(dx), abs(dy), "")
            return result.distinct()
        }

        return generateDirectionalSequence(dx, dy)
    }

    private fun calculateComplexity(codes: List<String>, depth: Int): Long = codes
        .sumOf { code ->
            val numericPart = code.filter { it.isDigit() }.toLong()
            dp(DpParams(code, depth, 0)) * numericPart
        }
}
