package aockt.y2024

import aockt.utils.CharGrid
import aockt.utils.Point
import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal
import io.github.jadarma.aockt.core.Solution

object Y2024D15 : Solution {
    private const val DEBUG = false
    private val t = if (DEBUG) Terminal(AnsiLevel.TRUECOLOR) else null

    override fun partOne(input: String): Int {
        val (grid, robot, moves) = parseWarehouseInput(input, false)
        val gpsSum = simulateWarehouse(grid, robot, moves)
        return gpsSum
    }

    override fun partTwo(input: String): Int {
        val (grid, robot, moves) = parseWarehouseInput(input, scaled = true)
        return simulateWarehouse(grid, robot, moves, isScaled = true)
    }

    enum class Direction(val delta: Pair<Int, Int>) {
        UP(Pair(0, 1)), RIGHT(Pair(1, 0)), DOWN(Pair(0, -1)), LEFT(Pair(-1, 0));

        fun move(pos: Point): Point = Point(pos.x + this.delta.first, pos.y + this.delta.second)
    }

    data class WarehouseInput(val grid: CharGrid, val robot: Point, val moves: List<Direction>)

    private fun parseWarehouseInput(input: String, scaled: Boolean): WarehouseInput {
        val parts = input.trim().replace("\r\n", "\n").split("\n\n")
        var gridData = parts[0]
        if (scaled) {
            gridData = scaleWarehouse(gridData)
        }
        val movesString = parts[1].replace("\n", "")

        val grid = CharGrid.fromString(gridData)
        var robotPosition = Point(0, 0)

        for ((point, value) in grid) {
            if (value == '@') {
                robotPosition = point
                grid[point] = '.'
            }
        }

        val moves = movesString.map {
            when (it) {
                '^' -> Direction.UP
                '>' -> Direction.RIGHT
                'v' -> Direction.DOWN
                '<' -> Direction.LEFT
                else -> error("Invalid move character: $it")
            }
        }

        return WarehouseInput(grid, robotPosition, moves)
    }

    private fun scaleWarehouse(grid: String): String {
        val scaledData = grid.flatMap { c ->
            when (c) {
                '#' -> listOf('#', '#')
                'O' -> listOf('[', ']')
                '.' -> listOf('.', '.')
                '@' -> listOf('@', '.')
                '\n' -> listOf('\n')
                else -> error("Invalid character in grid: '$c'")
            }
        }
        return scaledData.joinToString("")
    }

    private fun simulateWarehouse(
        grid: CharGrid, robot: Point, moves: List<Direction>, isScaled: Boolean = false
    ): Int {
        val steps = mutableMapOf<Int, String>()
        var step = 0

        var robotPos = robot

        var moveNum = 0

        val boxCount = if (DEBUG) grid.boxCount() else 0

        for (move in moves) {
            val newRobotPos = move.move(robotPos)

            if (!grid.isValidPosition(newRobotPos)) continue

            when (grid[newRobotPos]) {
                '.' -> {
                    robotPos = newRobotPos
                }

                ']' -> {
                    when {
                        (isScaled && (move == Direction.UP || move == Direction.DOWN)) -> {
                            val currentPos = Direction.LEFT.move(newRobotPos)
                            val boxesToMove = mutableListOf<Point>()

                            findBoxes(grid, currentPos, boxesToMove, move)

                            if (!boxesToMove.any {
                                    !movePossible(
                                        grid, boxesToMove, it, move
                                    )
                                }) {
                                if (move == Direction.UP) {
                                    boxesToMove.sortedByDescending { it.y }.forEach { moveBox(grid, it, move) }
                                } else {
                                    boxesToMove.sortedBy { it.y }.forEach { moveBox(grid, it, move) }
                                }
                                robotPos = newRobotPos
                            }
                        }

                        (isScaled && move == Direction.LEFT) -> {
                            var currentPos = move.move(newRobotPos)
                            val boxesToMove = mutableListOf<Point>()

                            while (grid[currentPos] == '[') {
                                val rightTile = Point(currentPos.x + 1, currentPos.y)
                                if (grid.isValidPosition(rightTile) && grid[rightTile] == ']') {
                                    boxesToMove.add(currentPos)
                                    currentPos = Point(currentPos.x - 2, currentPos.y)
                                } else {
                                    break
                                }
                            }

                            val lastFreeSpace = Point(currentPos.x + 1, currentPos.y)
                            if (grid.isValidPosition(lastFreeSpace) && grid[lastFreeSpace] == '.') {
                                boxesToMove.asReversed().forEach { boxPos ->
                                    grid[boxPos] = ']'
                                    grid[Point(boxPos.x - 1, boxPos.y)] = '['
                                }
                                grid[newRobotPos] = '.'
                                robotPos = newRobotPos
                            }

                        }
                    }
                }

                '[' -> {
                    when {
                        (isScaled && (move == Direction.UP || move == Direction.DOWN)) -> {
                            var currentPos = newRobotPos
                            val boxesToMove = mutableListOf<Point>()

                            findBoxes(grid, currentPos, boxesToMove, move)

                            if (!boxesToMove.any {
                                    !movePossible(
                                        grid, boxesToMove, it, move
                                    )
                                }) {
                                if (move == Direction.UP) {
                                    boxesToMove.sortedByDescending { it.y }.forEach { moveBox(grid, it, move) }
                                } else {
                                    boxesToMove.sortedBy { it.y }.forEach { moveBox(grid, it, move) }
                                }
                                robotPos = newRobotPos
                            }
                        }

                        (isScaled && move == Direction.RIGHT) -> {
                            var currentPos = newRobotPos
                            val boxesToMove = mutableListOf<Point>()

                            while (grid[currentPos] == '[') {
                                val rightTile = Point(currentPos.x + 1, currentPos.y)
                                if (grid.isValidPosition(rightTile) && grid[rightTile] == ']') {
                                    boxesToMove.add(currentPos)
                                    currentPos = Point(currentPos.x + 2, currentPos.y)
                                } else {
                                    break
                                }
                            }

                            val lastFreeSpace = currentPos
                            if (grid.isValidPosition(lastFreeSpace) && grid[lastFreeSpace] == '.') {
                                boxesToMove.asReversed().forEach { boxPos ->
                                    grid[boxPos] = ']'
                                    grid[Point(boxPos.x + 1, boxPos.y)] = '['
                                }
                                grid[lastFreeSpace] = ']'
                                grid[newRobotPos] = '.'
                                robotPos = newRobotPos
                            }

                        }
                    }
                }

                'O' -> {
                    if (!isScaled) {
                        var pos = robotPos
                        val lastBox = generateSequence {
                            pos = move.move(pos); pos
                        }.takeWhile { grid.isValidPosition(it) && grid[it] == 'O' }.lastOrNull()

                        if (lastBox != null) {
                            val newBoxPos = move.move(lastBox)
                            if (grid.isValidPosition(newBoxPos) && grid[newBoxPos] == '.') {
                                grid[newBoxPos] = 'O'
                                grid[newRobotPos] = '.'
                                robotPos = newRobotPos
                            }
                        }
                    }
                }
            }
            if (DEBUG) {
                ++moveNum
                if (moveNum >= moves.lastIndex) {
                    t?.println("After Move $moveNum $move")
                    grid.render(robotPos)
                }
                if (grid.boxCount() != boxCount) {
                    if (grid.boxCount() < boxCount) {
                        error("A box vanished in move $moveNum, ending")
                    }
                    error("A box appeared in move $moveNum, ending")
                }
                if (grid.hasError()) {
                    error("Error in Grid in move $moveNum, ending")
                }
            }
        }

        return grid.sumOf { (point, value) ->
            if (value == 'O' || value == '[') 100 * (grid.rowindices.last - point.y) + point.x else 0
        }
    }

    private fun CharGrid.boxCount(): Int {
        val boxRegex = "\\[]".toRegex()
        return this.data.sumOf { boxRegex.findAll(it.joinToString("")).count() }
    }

    private fun CharGrid.hasError(): Boolean {
        val failRegex = "[^\\[]\\]|\\[[^\\]]".toRegex()
        return this.data.any { failRegex.containsMatchIn(it.joinToString("")) }
    }

    private fun movePossible(grid: CharGrid, boxesToMove: MutableList<Point>, box: Point, move: Direction): Boolean {
        val pos1 = move.move(box)
        val pos2 = Direction.RIGHT.move(pos1)
        if (grid[pos1] == '#' || grid[pos2] == '#') {
            return false
        }
        return boxesToMove.contains(pos1) || boxesToMove.contains(Direction.LEFT.move(pos1)) || boxesToMove.contains(
            Direction.RIGHT.move(pos1)
        )
                || (grid.isValidPosition(pos1) && grid.isValidPosition(pos2) && grid[pos1] == '.' && grid[pos2] == '.')
    }

    private fun moveBox(grid: CharGrid, box: Point, move: Direction) {
        val pos1 = move.move(box)
        val pos2 = Direction.RIGHT.move(pos1)
        val old2 = Direction.RIGHT.move(box)
        grid[pos1] = '['
        grid[pos2] = ']'
        grid[box] = '.'
        grid[old2] = '.'
    }

    private fun findBoxes(
        grid: CharGrid, pos: Point, boxesToMove: MutableList<Point>, move: Direction
    ) {
        addBox(pos, grid, boxesToMove, move)
    }

    private fun addBox(
        currentPos: Point, grid: CharGrid, boxesToMove: MutableList<Point>, move: Direction
    ) {
        val rightTile = Point(currentPos.x + 1, currentPos.y)
        if (grid[currentPos] == '[' && grid.isValidPosition(rightTile) && grid[rightTile] == ']') {
            boxesToMove.add(currentPos)
            addBox(move.move(currentPos), grid, boxesToMove, move)
            addBox(Direction.RIGHT.move(move.move(currentPos)), grid, boxesToMove, move)
            addBox(Direction.LEFT.move(move.move(currentPos)), grid, boxesToMove, move)
        }
    }

    private fun CharGrid.render(robot: Point) {
        data.forEachIndexed { row, rowdata ->
            rowdata.forEachIndexed { col, c ->
                if (col == robot.x && (this.rowindices.last - row) == robot.y) t?.print(
                    brightRed("@")
                ) else when (c) {
                    '[' -> t?.print(yellow(c.toString()))
                    ']' -> t?.print(yellow(c.toString()))
                    '#' -> t?.print(gray(c.toString()))
                    '.' -> t?.print(green(c.toString()))
                    else -> t?.print(c.toString())
                }
            }
            t?.println("")
        }
        t?.println("")
    }
}
