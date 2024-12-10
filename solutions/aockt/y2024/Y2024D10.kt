package aockt.y2024

import io.github.jadarma.aockt.core.Solution
import java.util.*

object Y2024D10 : Solution {
    override fun partOne(input: String): Int {
        val grid = parseInput(input)

        return grid.indices.sumOf { row ->
            grid[row].indices
                .filter { grid[row][it] == 0 }
                .sumOf { bfs(grid, Pair(row, it)) }
        }
    }

    override fun partTwo(input: String): Int {
        val grid = parseInput(input)

        return grid.indices.sumOf { row ->
            grid[row].indices
                .filter { grid[row][it] == 0 }
                .sumOf { dfs(grid, Pair(row, it), 0, mutableMapOf()) }
        }
    }

    private fun parseInput(input: String): List<List<Int>> {
        return input.lines().map { line -> line.map { it.digitToInt() } }
    }

    private val directions = listOf(
        Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)
    )

    private fun bfs(grid: List<List<Int>>, start: Pair<Int, Int>): Int {

        val visited = mutableSetOf<Pair<Int, Int>>()
        val queue = ArrayDeque<Pair<Pair<Int, Int>, Int>>()
        queue.add(start to 0)
        val reachable = mutableSetOf<Pair<Int, Int>>()

        while (queue.isNotEmpty()) {
            val (current, currentHeight) = queue.removeFirst()
            if (current in visited) continue
            visited.add(current)

            if (grid[current.first][current.second] == 9) {
                reachable.add(current)
                continue
            }

            for (dir in directions) {
                val neighbor = Pair(current.first + dir.first, current.second + dir.second)
                if (
                    isInBounds(neighbor, grid) &&
                    neighbor !in visited &&
                    grid[neighbor.first][neighbor.second] == currentHeight + 1
                ) {
                    queue.add(neighbor to grid[neighbor.first][neighbor.second])
                }
            }
        }

        return reachable.size
    }

    private fun dfs(
        grid: List<List<Int>>,
        current: Pair<Int, Int>,
        currentHeight: Int,
        memo: MutableMap<Pair<Int, Int>, Int>
    ): Int {
        if (grid[current.first][current.second] == 9) return 1
        if (current in memo) return memo[current]!!

        var trailCount = 0
        for (dir in directions) {
            val neighbor = Pair(current.first + dir.first, current.second + dir.second)
            if (
                isInBounds(neighbor, grid) &&
                grid[neighbor.first][neighbor.second] == currentHeight + 1
            ) {
                trailCount += dfs(grid, neighbor, currentHeight + 1, memo)
            }
        }

        memo[current] = trailCount
        return trailCount
    }

    private fun isInBounds(
        point: Pair<Int, Int>,
        grid: List<List<Int>>
    ) = point.first in grid.indices && point.second in grid[0].indices

}
