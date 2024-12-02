package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D01 : Solution {
    override fun partOne(input: String): Int {
        val (leftList, rightList) = parseList(input)

        val sortedLeft = leftList.sorted()
        val sortedRight = rightList.sorted()

        return sortedLeft.zip(sortedRight) { left, right ->
            kotlin.math.abs(left - right)
        }.sum()
    }

    override fun partTwo(input: String): Int {
        val (leftList, rightList) = parseList(input)

        val sortedLeft = leftList.sorted()
        val sortedRight = rightList.sorted()

        return sortedLeft.sumOf { it * sortedRight.count { el -> el == it } }
    }

    private fun parseList(input: String): Pair<List<Int>, List<Int>> {
        val pairs = input.trim().lines().map { line ->
            val (left, right) = line.split("\\s+".toRegex()).map { it.toInt() }
            left to right
        }

        val leftList = pairs.map { it.first }
        val rightList = pairs.map { it.second }
        return Pair(leftList, rightList)
    }
}

