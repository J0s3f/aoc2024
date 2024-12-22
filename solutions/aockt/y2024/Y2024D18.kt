package aockt.y2024

import aockt.utils.Point
import io.github.jadarma.aockt.core.Solution
import java.util.*

object Y2024D18 : Solution {

    override fun partOne(input: String): Int {
        val gridSize = 71
        val grid = Array(gridSize) { Array(gridSize) { Corrupted(false) } }
        val bytes = parseInput(input)

        bytes.take(1024).forEach { (x, y) ->
            grid[y][x] = Corrupted(true)
        }

        val start = Point(0, 0)
        val end = Point(gridSize - 1, gridSize - 1)
        return findShortestPath(grid, start, end) ?: -1
    }

    override fun partTwo(input: String): String {
        val gridSize = 71
        val grid = Array(gridSize) { Array(gridSize) { Corrupted(false) } }
        val bytes = parseInput(input)

        val start = Point(0, 0)
        val end = Point(gridSize - 1, gridSize - 1)

        bytes.forEach { (x, y) ->
            grid[y][x] = Corrupted(true)
            if (findShortestPath(grid, start, end) == null) {
                return "$x,$y"
            }
        }

        error("No blockage found")
    }

    @JvmInline
    value class Corrupted(private val value: Boolean) {
        fun isCorrupted() = value
    }

    private fun parseInput(input: String): List<Point> {
        return input.lines().filter { it.isNotBlank() }.map { line ->
            val (x, y) = line.split(",").map { it.trim().toInt() }
            Point(x, y)
        }
    }

    private fun findShortestPath(grid: Array<Array<Corrupted>>, start: Point, end: Point): Int? {
        val directions = listOf(
            Point(0, 1),
            Point(1, 0),
            Point(0, -1),
            Point(-1, 0)
        )

        val queue = LinkedList<Pair<Point, Int>>()
        val visited = mutableSetOf<Point>()

        queue.add(start to 0)
        visited.add(start)

        while (queue.isNotEmpty()) {
            val (current, steps) = queue.poll()

            if (current == end) return steps

            for (dir in directions) {
                val neighbor = current + dir

                if (neighbor.isValid(grid.size)
                    && !grid[neighbor.y][neighbor.x].isCorrupted() && neighbor !in visited
                ) {
                    queue.add(neighbor to steps + 1)
                    visited.add(neighbor)
                }
            }
        }

        return null
    }

    private fun Point.isValid(sizeX: Int, sizeY: Int = sizeX) = x in 0 until sizeX && y in 0 until sizeY
}
