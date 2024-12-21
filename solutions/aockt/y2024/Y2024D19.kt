package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D19 : Solution {

    override fun partOne(input: String): Int {
        val (patterns, designs) = parseInput(input)
        return designs.count { canFormDesign(it, patterns) }
    }

    override fun partTwo(input: String): Long {
        val (patterns, designs) = parseInput(input)
        return designs.sumOf { countWaysToFormDesign(it, patterns) }
    }

    private fun parseInput(input: String): Pair<List<String>, List<String>> {
        val lines = input.lines().filter { it.isNotBlank() }
        val patterns = lines.first().split(", ").map { it.trim() }
        val designs = lines.drop(1)
        return patterns to designs
    }

    private fun canFormDesign(design: String, patterns: List<String>): Boolean {
        val dp = BooleanArray(design.length + 1) { false }
        dp[0] = true

        for (i in 1..design.length) {
            for (pattern in patterns) {
                if (i >= pattern.length && design.substring(i - pattern.length, i) == pattern) {
                    dp[i] = dp[i] || dp[i - pattern.length]
                }
            }
        }

        return dp[design.length]
    }

    private fun countWaysToFormDesign(design: String, patterns: List<String>): Long {
        val dp = LongArray(design.length + 1) { 0 }
        dp[0] = 1

        for (i in 1..design.length) {
            for (pattern in patterns) {
                if (i >= pattern.length && design.substring(i - pattern.length, i) == pattern) {
                    dp[i] += dp[i - pattern.length]
                }
            }
        }

        return dp[design.length]
    }
}
