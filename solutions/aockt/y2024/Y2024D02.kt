package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D02 : Solution {
    override fun partOne(input: String): Int {
        val reports = parseList(input)

        return reports.count { isSafe(it) }
    }

    override fun partTwo(input: String): Int {
        val reports = parseList(input)
        return reports.count { isSafeWithDampener(it) }
    }

    private fun isSafe(report: List<Int>): Boolean {
        val pairs = report.zipWithNext()
        return pairs.all { isSafeInc(it) } || pairs.all { isSafeDec(it) }
    }

    private fun isSafeWithDampener(report: List<Int>): Boolean {
        if (isSafe(report)) return true

        return report.indices.any { index ->
            val modifiedReport = report.filterIndexed { i, _ -> i != index }
            isSafe(modifiedReport)
        }
    }

    private fun isSafeInc(report: Pair<Int, Int>): Boolean {
        val difference = report.first - report.second
        return !(difference < 1 || difference > 3)
    }

    private fun isSafeDec(report: Pair<Int, Int>): Boolean {
        val difference = report.second - report.first
        return !(difference < 1 || difference > 3)
    }

    private fun parseList(input: String): List<List<Int>> {
        return input.trim().lines().map { line ->
            line.split("\\s+".toRegex()).map { it.toInt() }
        }
    }
}



