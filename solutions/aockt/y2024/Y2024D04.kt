package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D04 : Solution {
    override fun partOne(input: String): Int {
        val grid = parseGrid(input)
        val word = "XMAS"

        var count = 0

        val directions = listOf(
            Pair(0, 1),
            Pair(0, -1),
            Pair(1, 0),
            Pair(-1, 0),
            Pair(1, 1),
            Pair(-1, -1),
            Pair(1, -1),
            Pair(-1, 1)
        )

        grid.indices.forEach { row ->
            grid[row].indices.forEach { col ->
                directions.forEach { (dx, dy) ->
                    when {
                        grid.findWord(word, row, col, dx, dy) -> {
                            count++
                        }
                    }
                }
            }
        }

        return count
    }

    override fun partTwo(input: String): Int {
        val grid = parseGrid(input)
        var count = 0

        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid.checkXMasPattern(row, col)) {
                    count++
                }
            }
        }

        return count
    }

    private fun parseGrid(input: String): Array<CharArray> {
        return input.trim().lines().map { it.trim().toCharArray() }.toTypedArray()
    }

    private fun Array<CharArray>.findWord(word: String, row: Int, col: Int, dx: Int, dy: Int): Boolean {
        var r = row
        var c = col

        for (i in word.indices) {
            if (r !in this.indices || c !in this[r].indices || this[r][c] != word[i]) {
                return false
            }
            r += dx
            c += dy
        }

        return true
    }

    private fun Array<CharArray>.checkXMasPattern(row: Int, col: Int): Boolean {
        if (this[row][col] != 'A') return false

        return arrayOf(
            checkForMAS(row, col, 1, 1),
            checkForMAS(row, col, -1, -1),
            checkForMAS(row, col, 1, -1),
            checkForMAS(row, col, -1, 1)
        ).count { it } >= 2
    }

    private fun Array<CharArray>.checkForMAS(row: Int, col: Int, dx: Int, dy: Int): Boolean {
        return (getChar(row - dx, col - dy) == 'M' &&
                getChar(row + dx, col + dy) == 'S')
    }

    private fun Array<CharArray>.getChar(row: Int, col: Int): Char {
        return if (row in this.indices && col in this[row].indices) this[row][col] else ' '
    }
}
