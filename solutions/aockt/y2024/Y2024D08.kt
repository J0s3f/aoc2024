package aockt.y2024

import aockt.utils.CharGrid
import aockt.utils.pairs
import io.github.jadarma.aockt.core.Solution

object Y2024D08 : Solution {
    override fun partOne(input: String): Int {
        val grid = parseGrid(input)
        val antennaPositions = collectAntennaPositions(grid)

        return antennaPositions.values.flatMap {
            it.pairs().flatMap { (firstAntenna, secondAntenna) ->
                grid.antinodesFrom(firstAntenna, secondAntenna)
            }
        }.toSet().size
    }

    override fun partTwo(input: String): Int {
        val grid = parseGrid(input)
        val antennaPositions = collectAntennaPositions(grid)

        return antennaPositions.values.flatMap {
            it.pairs().flatMap { (firstAntenna, secondAntenna) ->
                grid.resonantHarmonicAntinodesFrom(firstAntenna, secondAntenna)
            }
        }.toSet().size
    }

    private fun parseGrid(input: String): CharGrid {
        return CharGrid(input.lines().map { it.toCharArray() }.toTypedArray())
    }

    private fun collectAntennaPositions(grid: CharGrid): Map<Char, MutableList<Pair<Int, Int>>> {
        val positionsByFrequency = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

        for (y in grid.rowindices) {
            for (x in grid[y].indices) {
                val char = grid[y][x]
                if (char.isLetterOrDigit()) {
                    positionsByFrequency.computeIfAbsent(char) { mutableListOf() }.add(Pair(x, y))
                }
            }
        }

        return positionsByFrequency
    }

    private fun CharGrid.antinodesFrom(a: Pair<Int, Int>, b: Pair<Int, Int>) = listOf(
        Pair(
            a.first + (b.first - a.first) * 2,
            a.second + (b.second - a.second) * 2
        ),
        Pair(
            b.first + (a.first - b.first) * 2,
            b.second + (a.second - b.second) * 2
        )
    ).filter { this.isValidPosition(it) }

    private fun CharGrid.resonantHarmonicAntinodesFrom(a: Pair<Int, Int>, b: Pair<Int, Int>): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        var cur = b
        while (isValidPosition(cur)) {
            result.add(cur)
            cur = Pair(
                cur.first + b.first - a.first,
                cur.second + b.second - a.second
            )
        }
        cur = a
        while (isValidPosition(cur)) {
            result.add(cur)
            cur = Pair(
                cur.first + a.first - b.first,
                cur.second + a.second - b.second
            )
        }
        return result
    }
}
