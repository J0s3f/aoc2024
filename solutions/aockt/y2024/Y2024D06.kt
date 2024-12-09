package aockt.y2024

import aockt.utils.CharGrid
import io.github.jadarma.aockt.core.Solution

object Y2024D06 : Solution {
    override fun partOne(input: String): Int {
        val grid = parseGrid(input)
        val (startRow, startCol, direction) = findGuardStart(grid)

        val visited = mutableSetOf<Pair<Int, Int>>()
        var row = startRow
        var col = startCol
        var dir = direction

        while (true) {
            visited.add(Pair(row, col))

            val (dRow, dCol) = dir.delta
            val nextRow = row + dRow
            val nextCol = col + dCol

            if (!grid.isValidPosition(nextRow, nextCol)) {
                break
            }

            if (grid[nextRow][nextCol] == '#') {
                dir = dir.turnRight()
            } else {
                row = nextRow
                col = nextCol
            }
        }

        return visited.size
    }

    override fun partTwo(input: String): Int {
        val grid = parseGrid(input)
        val (startRow, startCol, direction) = findGuardStart(grid)

        val openPositions = grid.data.mapIndexed { row, line ->
            line.mapIndexed { col, char ->
                if (char == '.' && !(row == startRow && col == startCol)) Pair(row, col) else null
            }
        }.flatten().filterNotNull()

        return openPositions.count { position ->
            val (row, col) = position
            val gridWithObstruction = grid.copy()
            gridWithObstruction[row][col] = '#'

            val visited = mutableSetOf<Triple<Int, Int, Direction>>()
            var r = startRow
            var c = startCol
            var dir = direction

            while (true) {
                if (!visited.add(Triple(r, c, dir))) {
                    return@count true
                }

                val (dr, dc) = dir.delta
                val nr = r + dr
                val nc = c + dc

                if (!gridWithObstruction.isValidPosition(nr, nc)) break
                if (gridWithObstruction[nr][nc] == '#') {
                    dir = dir.turnRight()
                } else {
                    r = nr
                    c = nc
                }
            }

            false
        }
    }

    private fun parseGrid(input: String): CharGrid {
        return CharGrid(input.lines().map { it.toCharArray() })
    }

    private fun findGuardStart(grid: CharGrid): Triple<Int, Int, Direction> {
        for (row in grid.data.indices) {
            for (col in grid[row].indices) {
                when (grid[row][col]) {
                    '^' -> return Triple(row, col, Direction.UP)
                    '>' -> return Triple(row, col, Direction.RIGHT)
                    'v' -> return Triple(row, col, Direction.DOWN)
                    '<' -> return Triple(row, col, Direction.LEFT)
                }
            }
        }
        throw IllegalArgumentException("Guard not found on the map")
    }

    enum class Direction(val delta: Pair<Int, Int>) {
        UP(Pair(-1, 0)), RIGHT(Pair(0, 1)), DOWN(Pair(1, 0)), LEFT(Pair(0, -1));

        fun turnRight(): Direction = entries[(ordinal + 1) % 4]
    }

}
