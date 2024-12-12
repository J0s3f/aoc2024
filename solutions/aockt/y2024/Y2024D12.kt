package aockt.y2024

import aockt.utils.CharGrid
import aockt.utils.Point
import io.github.jadarma.aockt.core.Solution

object Y2024D12 : Solution {
    override fun partOne(input: String): Long = CharGrid.fromString(input).calculateTotalFencePrice()

    override fun partTwo(input: String): Long = CharGrid.fromString(input).calculateDiscountFencePrice()

    private fun CharGrid.calculateTotalFencePrice(): Long {
        var totalPrice = 0L
        val visited = mutableSetOf<Point>()
        for (pos in this) {
            if (!visited.contains(pos.point)) {
                val (area, perimeter) = bfs(pos.point, visited)
                totalPrice += area * perimeter
            }
        }

        return totalPrice
    }

    private fun CharGrid.bfs(start: Point, visited: MutableSet<Point>): Pair<Int, Int> {
        val plantType = this[start]
        val queue = ArrayDeque<Point>()
        val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))
        queue.add(start)
        visited.add(start)

        var area = 0
        var perimeter = 0

        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            area++

            for ((dx, dy) in directions) {
                val n = Point(x + dx, y + dy)

                if (!this.isValidPosition(n) || this[n] != plantType) {
                    perimeter++
                } else if (!visited.contains(n)) {
                    visited.add(n)
                    queue.add(n)
                }
            }
        }

        return area to perimeter
    }

    private fun CharGrid.calculateDiscountFencePrice(): Long {
        var totalPrice = 0L
        val visited = mutableSetOf<Point>()

        for (pos in this) {
            if (!visited.contains(pos.point)) {
                val (area, corners) = bfsDiscount(pos.point, visited)
                totalPrice += area * corners
            }
        }
        return totalPrice
    }


    private fun CharGrid.bfsDiscount(start: Point, visited: MutableSet<Point>): Pair<Int, Int> {
        val plantType = this[start]
        val queue = ArrayDeque<Point>()
        val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))
        queue.add(start)
        visited.add(start)

        var area = 0
        var corners = 0

        while (queue.isNotEmpty()) {
            val pos = queue.removeFirst()
            val (x, y) = pos
            area++

            for ((dx, dy) in directions) {
                val n = Point(x + dx, y + dy)

                if (this.isValidPosition(n) && this[n] == plantType && !visited.contains(n)) {
                    visited.add(n)
                    queue.add(n)
                }
            }
            corners += (directions + directions.first()).windowed(2).filter {
                val a = this[pos]
                val b = try {
                    this[Point(x + it.first().first, y + it.first().second)]
                } catch (_: IndexOutOfBoundsException) {
                    null
                }
                val c = try {
                    this[Point(x + it.last().first, y + it.last().second)]
                } catch (_: IndexOutOfBoundsException) {
                    null
                }
                val d = try {
                    this[Point(x + it.first().first + it.last().first, y + it.first().second + it.last().second)]
                } catch (_: IndexOutOfBoundsException) {
                    null
                }
                (a != b && a != c) || (a == b && a == c && a != d)
            }.size
        }

        return area to corners
    }

}

