package aockt.y2024

import aockt.utils.CharGrid
import aockt.utils.Point
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

    private fun collectAntennaPositions(grid: CharGrid): Map<Char, MutableList<Point>> {
        val positionsByFrequency = mutableMapOf<Char, MutableList<Point>>()

        for (pos in grid) {
            if (pos.value.isLetterOrDigit()) {
                positionsByFrequency.computeIfAbsent(pos.value) { mutableListOf() }.add(pos.point)
            }
        }

        return positionsByFrequency
    }

    private fun CharGrid.antinodesFrom(a: Point, b: Point) = listOf(
        Point(
            a.x + (b.x - a.x) * 2,
            a.y + (b.y - a.y) * 2
        ),
        Point(
            b.x + (a.x - b.x) * 2,
            b.y + (a.y - b.y) * 2
        )
    ).filter { this.isValidPosition(it) }

    private fun CharGrid.resonantHarmonicAntinodesFrom(a: Point, b: Point): List<Point> {
        val result = mutableListOf<Point>()
        var cur = b
        while (isValidPosition(cur)) {
            result.add(cur)
            cur = Point(
                cur.x + b.x - a.x,
                cur.y + b.y - a.y
            )
        }
        cur = a
        while (isValidPosition(cur)) {
            result.add(cur)
            cur = Point(
                cur.x + a.x - b.x,
                cur.y + a.y - b.y
            )
        }
        return result
    }
}
