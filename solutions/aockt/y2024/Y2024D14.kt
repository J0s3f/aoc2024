package aockt.y2024

import io.github.jadarma.aockt.core.Solution
import java.util.regex.Pattern

object Y2024D14 : Solution {
    private val printResult = false
    override fun partOne(input: String): Int {
        val robots = parseRobots(input)

        val width = 101
        val height = 103
        val seconds = 100

        simulateRobots(robots, seconds, width, height)
        return calculateSafetyFactor(robots, width, height)
    }

    override fun partTwo(input: String): Int {
        val robots = parseRobots(input)

        val width = 101
        val height = 103

        val seconds = findPattern(robots, width, height)
        return seconds
    }

    private data class Robot(var x: Int, var y: Int, val vx: Int, val vy: Int)

    private fun parseRobots(input: String): List<Robot> = input.lines().mapNotNull(::parseRobot)

    private fun parseRobot(line: String): Robot? {
        val matcher = Pattern.compile("p=(?<px>-?\\d+),(?<py>-?\\d+) v=(?<vx>-?\\d+),(?<vy>-?\\d+)").matcher(line)
        return if (matcher.find()) {
            val px = matcher.group("px").toInt()
            val py = matcher.group("py").toInt()
            val vx = matcher.group("vx").toInt()
            val vy = matcher.group("vy").toInt()
            Robot(px, py, vx, vy)
        } else null
    }

    private fun simulateRobots(robots: List<Robot>, seconds: Int, width: Int, height: Int) {
        robots.forEach { robot ->
            robot.x = (robot.x + robot.vx * seconds).mod(width)
            if (robot.x < 0) robot.x += width

            robot.y = (robot.y + robot.vy * seconds).mod(height)
            if (robot.y < 0) robot.y += height
        }
    }

    private fun calculateSafetyFactor(robots: List<Robot>, width: Int, height: Int): Int {
        val midX = width / 2
        val midY = height / 2

        var topLeft = 0
        var topRight = 0
        var bottomLeft = 0
        var bottomRight = 0

        robots.forEach { robot ->
            when {
                robot.x < midX && robot.y < midY -> topLeft++
                robot.x > midX && robot.y < midY -> topRight++
                robot.x < midX && robot.y > midY -> bottomLeft++
                robot.x > midX && robot.y > midY -> bottomRight++
            }
        }

        return topLeft * topRight * bottomLeft * bottomRight
    }

    private fun drawRobots(robots: List<Robot>, width: Int, height: Int): Array<CharArray> {
        val grid = Array(height) { CharArray(width) { '.' } }
        robots.forEach { robot ->
            grid[robot.y][robot.x] = '#'
        }
        return grid
    }

    private fun computeLongestLine(robots: List<Robot>, width: Int, height: Int): Int {
        val grid = drawRobots(robots, width, height)

        var longestLine = 0
        for (i in grid.indices) {
            var line = 0
            for (j in grid[0].indices) {
                if (grid[i][j] == '#') {
                    line++
                    longestLine = maxOf(longestLine, line)
                } else {
                    line = 0
                }
            }
        }
        return longestLine
    }


    private fun findPattern(robots: List<Robot>, width: Int, height: Int): Int {
        var time = 0
        var longestLine = 0
        var bestTime = 0

        while (time < width * height) {
            simulateRobots(robots, 1, width, height)
            time++

            val longestLineThisIteration = computeLongestLine(robots, width, height)

            if (longestLineThisIteration > longestLine) {
                longestLine = longestLineThisIteration
                bestTime = time
            }
        }

        if (printResult) {
            simulateRobots(robots, -1 * (time - bestTime), width, height)
            println("Pattern at second $bestTime:")
            drawRobots(robots, width, height).forEach { println(it.concatToString()) }
        }

        return bestTime
    }
}
