package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D11 : Solution {
    override fun partOne(input: String): Int {
        var stones = parseInput(input)

        repeat(25) {
            stones = transformStones(stones)
        }

        return stones.size
    }

    override fun partTwo(input: String): Long {
        return simulateBlinks(input, 75)
    }

    private fun parseInput(input: String) = input.trim().split(" ").map { it.toLong() }.toList()

    private fun transformStones(stones: List<Long>) = stones.flatMap { transformStone(it) }

    private fun transformStone(stone: Long): Sequence<Long> {
        return when {
            stone == 0L -> sequenceOf(1L)
            stone.toString().length % 2 == 0 -> {
                val digits = stone.toString()
                val mid = digits.length / 2
                sequenceOf(digits.substring(0, mid).toLong(), digits.substring(mid).toLong())
            }

            else -> sequenceOf(stone * 2024)
        }
    }

    private fun parseToMap(input: String) = parseInput(input)
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
        .toMutableMap()

    private fun simulateBlinks(input: String, totalBlinks: Int): Long {
        var stoneCounts = parseToMap(input)

        repeat(totalBlinks) {
            val newStoneCounts = mutableMapOf<Long, Long>()
            for ((stone, count) in stoneCounts) {
                newStoneCounts.merge(transformStone(stone), count)
            }
            stoneCounts = newStoneCounts
        }

        return stoneCounts.values.sum()
    }


    private fun MutableMap<Long, Long>.merge(stones: Sequence<Long>, count: Long) =
        stones.forEach { this[it] = (this[it] ?: 0L) + count }

}


