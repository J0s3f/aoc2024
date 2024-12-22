package aockt.y2024

import aockt.utils.CharGrid
import aockt.utils.Direction
import aockt.utils.Point
import io.github.jadarma.aockt.core.Solution
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val WALL = '#'

object Y2024D20 : Solution {

    override fun partOne(input: String): Int {
        val (grid, start, end) = parseInput(input)

        val pathWithoutCheats = grid.bfs(start, end)

        return countCheats(grid, pathWithoutCheats, 2)
    }

    override fun partTwo(input: String): Int {
        val (grid, start, end) = parseInput(input)

        val pathWithoutCheats = grid.bfs(start, end)

        return countCheats(grid, pathWithoutCheats, 20)
    }

    private fun parseInput(input: String): Triple<CharGrid, Point, Point> {
        val grid = CharGrid.fromString(input.trim())
        val start = grid.first { it.value == 'S' }.point
        val end = grid.first { it.value == 'E' }.point
        return Triple(grid, start, end)
    }

    private fun CharGrid.bfs(start: Point, end: Point): List<Point> {
        val queue: Deque<Pair<Point, List<Point>>> = ArrayDeque()
        val visited = hashSetOf<Point>()

        queue.offer(start to listOf(start))
        visited.add(start)

        while (queue.isNotEmpty()) {
            val (current, path) = queue.poll()

            if (current == end) {
                return path
            }

            for (dir in Direction.entries) {
                val next = current + dir
                if (isValidPosition(next) && next !in visited && this[next] != WALL) {
                    visited.add(next)
                    queue.offer(next to path + next)
                }
            }
        }
        error("No path found")
    }

    private fun countCheats(
        grid: CharGrid,
        path: List<Point>,
        maxCheatTime: Int,
        minTimeSaved: Int = 100
    ): Int {
        data class Cheat(val start: Point, val end: Point, val duration: Int, val saved: Int)

        val pathMap = path.mapIndexed { idx, point -> point to idx }.toMap()
        val cheats = mutableSetOf<Cheat>()

        for ((idx, cheatLocation) in path.withIndex()) {
            val skipPath = path.subList(idx, min(idx + maxCheatTime + 1, path.size))
            val environment = grid.findPointsWithMaxDistance(maxCheatTime, cheatLocation)
                .filter { it.key != cheatLocation && it.key in pathMap.keys }
            environment.forEach { candidate ->
                val savedDistance =
                    pathMap[candidate.key]!! - pathMap[skipPath[min(candidate.value, skipPath.size - 1)]]!!
                if (savedDistance > 0) {
                    cheats.add(Cheat(cheatLocation, candidate.key, candidate.value, savedDistance))
                }
            }
        }

        return cheats.filter { it.saved >= minTimeSaved }.size
    }

    private fun CharGrid.findPointsWithMaxDistance(maxDistance: Int, start: Point): Map<Point, Int> {
        val result = mutableMapOf<Point, Int>()

        val minX = max(0, start.x - maxDistance)
        val maxX = min(colindices.last, start.x + maxDistance)
        val minY = max(0, start.y - maxDistance)
        val maxY = min(rowindices.last, start.y + maxDistance)

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                val distance = abs(start.x - x) + abs(start.y - y)
                if (distance <= maxDistance) {
                    val point = Point(x, y)
                    if (isValidPosition(point)) {
                        result[point] = distance
                    }
                }
            }
        }
        return result
    }
}
